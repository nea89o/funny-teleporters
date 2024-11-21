package moe.nea.funnyteleporters;

import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.elements.GuiElementBuilderInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Map;

public class TeleporterNexusScreen extends SimpleGui {
	private final TeleporterNexusBlockEntity blockEntity;

	public TeleporterNexusScreen(TeleporterNexusBlockEntity blockEntity, ServerPlayerEntity player) {
		super(ScreenHandlerType.GENERIC_9X6, player, false);
		this.blockEntity = blockEntity;
		setSlots();
	}

	void setSlots() {
		for (int i = 0; i < 9; i++) {
			setSlot(i, Utils.createBlankBlack());
			setSlot(i + (height - 1) * width, Utils.createBlankBlack());
		}
		var it = blockEntity.destinations.entrySet().iterator();
		for (int i = 1; i < height - 1 && it.hasNext(); i++) {
			for (int j = 0; j < width && it.hasNext(); j++) {
				int index = i * width + j;
				var entry = it.next();
				setSlot(index, getHandlerForEntry(entry));
			}
		}
	}

	private GuiElementBuilderInterface<?> getHandlerForEntry(Map.Entry<TeleporterDestination, RegistryEntry<Item>> entry) {
		var dest = entry.getKey();
		return GuiElementBuilder.from(new ItemStack(entry.getValue()))
		                        .setName(Text.literal("Teleport to"))
		                        .hideDefaultTooltip()
		                        .addLoreLine(Text.literal(String.format("x: %d, y: %d, z: %d", dest.blockPos().getX(), dest.blockPos().getY(), dest.blockPos().getZ())))
		                        .addLoreLine(Text.literal("in " + dest.target().getValue()))
		                        .setCallback((clickType) -> {
			                        if (clickType == ClickType.MOUSE_RIGHT) {
										new TeleporterNexusEditorScreen(blockEntity, dest, player).open();
				                        return;
			                        }
			                        dest.teleport(player);
			                        close();
		                        });
	}
}
