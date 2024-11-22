package moe.nea.funnyteleporters;

import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.elements.GuiElementBuilderInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Map;

public class TeleporterNexusScreen extends SimpleGui {
	private final TeleporterNexusBlockEntity blockEntity;

	public TeleporterNexusScreen(TeleporterNexusBlockEntity blockEntity, ServerPlayerEntity player) {
		super(ScreenHandlerType.GENERIC_9X6, player, false);
		this.blockEntity = blockEntity;
		setSlots();
		setTitle(Text.literal("Teleport Nexus"));
	}

	int scroll = 0;

	void setSlots() {
		for (int i = 0; i < 9; i++) {
			setSlot(i, Utils.createBlankBlack());
			setSlot(i + (height - 1) * width, Utils.createBlankBlack());
		}
		if (scroll > 0) {
			setSlot(0, GuiElementBuilder.from(new ItemStack(Items.ARROW))
			                            .setName(Text.literal("Previous page"))
			                            .setCallback(() -> scroll(-1)));
		}
		var it = blockEntity.destinations.entrySet().iterator();
		Utils.skipIt(it, scroll * width * (height - 2));
		for (int i = 1; i < height - 1 && it.hasNext(); i++) {
			for (int j = 0; j < width && it.hasNext(); j++) {
				int index = i * width + j;
				var entry = it.next();
				setSlot(index, getHandlerForEntry(entry));
			}
		}
		if (it.hasNext()) {
			setSlot(1, GuiElementBuilder.from(new ItemStack(Items.ARROW))
			                            .setName(Text.literal("Next page"))
			                            .setCallback(() -> scroll(1)));
		}
	}

	void scroll(int offset) {
		scroll = Math.max(0, scroll + offset);
		setSlots();
	}

	void handleClick(ClickType clickType, TeleporterDestination destination) {
		if (clickType == ClickType.MOUSE_RIGHT) {
			new TeleporterNexusEditorScreen(blockEntity, destination, player).open();
			return;
		}
		destination.teleport(player);
		close();
	}

	private GuiElementBuilderInterface<?> getHandlerForEntry(Map.Entry<TeleporterDestination, TeleporterNexusBlockEntity.Label> entry) {
		var dest = entry.getKey();
		return GuiElementBuilder.from(new ItemStack(entry.getValue().item()))
		                        .setName(Text.literal("Teleport to" + entry.getValue().label().map(it -> " " + it).orElse("")))
		                        .hideDefaultTooltip()
		                        .addLoreLine(Text.literal(String.format("x: %d, y: %d, z: %d", dest.blockPos().getX(), dest.blockPos().getY(), dest.blockPos().getZ()))
		                                         .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.AQUA)))
		                        .addLoreLine(Text.literal("in " + dest.target().getValue())
		                                         .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.AQUA)))
		                        .addLoreLine(Text.empty())
		                        .addLoreLine(Text.literal("Left-Click to teleport.").setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.GRAY)))
		                        .addLoreLine(Text.literal("Right-Click to edit item.").setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.GRAY)))
		                        .setCallback(clickType -> handleClick(clickType, dest));
	}
}
