package moe.nea.funnyteleporters;

import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;

public class ColouredChestViewScreen extends SimpleGui {
	private final ColouredChestBlockEntity blockEntity;

	public ColouredChestViewScreen(ServerPlayerEntity player, ColouredChestBlockEntity blockEntity) {
		super(ScreenHandlerType.GENERIC_9X4, player, false);
		this.blockEntity = blockEntity;
		setSlots();
	}

	@Override
	public boolean onClick(int index, ClickType type, SlotActionType action, GuiElementInterface element) {
		if (index == 0)
			return new ColouredChestConfigGUI(blockEntity, player).open();
		return super.onClick(index, type, action, element);
	}

	void setSlots() {
		for (int i = 1; i < 9; i++) {
			var s = new ItemStack(Items.BLACK_STAINED_GLASS_PANE);
			s.set(DataComponentTypes.CUSTOM_NAME, Text.empty());
			s.set(DataComponentTypes.HIDE_TOOLTIP, Unit.INSTANCE);
			setSlot(i, s);
		}
		setSlot(0, new ItemStack(Items.ARROW));
		for (int i = 0; i < 27; i++) {
			setSlotRedirect(i + 9, new Slot(blockEntity, i, 0, 0));
		}
	}
}
