package moe.nea.funnyteleporters;

import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TeleporterNexusEditorScreen extends SimpleGui {
	private final TeleporterNexusBlockEntity blockEntity;
	private final TeleporterDestination dest;

	public TeleporterNexusEditorScreen(TeleporterNexusBlockEntity blockEntity, TeleporterDestination dest, ServerPlayerEntity player) {
		super(ScreenHandlerType.GENERIC_9X3, player, false);
		this.blockEntity = blockEntity;
		this.dest = dest;
		setTitle(Text.literal("Set Icon for Teleporter"));
		setSlots();
	}

	void setSlots() {
		for (int i = 0; i < width * height; i++) {
			setSlot(i, Utils.createBlankBlack());
		}
		setSlot(width * height / 2,
		        GuiElementBuilder.from(new ItemStack(blockEntity.destinations.getOrDefault(dest, Registries.ITEM.getEntry(Items.ENDER_PEARL)).value()))
		                         .setName(Text.literal("Click item in your inventory to set icon.")));
	}

	@Override
	public boolean onAnyClick(int index, ClickType type, SlotActionType action) {
		System.out.println("Index: " + index);
		var slot = getSlotRedirectOrPlayer(index);
		if (slot == null) return true;
		var stack = slot.getStack();
		if (!stack.isEmpty()) {
			blockEntity.setIcon(dest, stack.getItem());
			new TeleporterNexusScreen(blockEntity, player).open();
		}
		return false;
	}
}
