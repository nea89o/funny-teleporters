package moe.nea.funnyteleporters;

import com.google.common.collect.ImmutableList;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Collections;

public class ColouredChestConfigGUI extends SimpleGui {

	private final ColouredChestBlockEntity blockEntity;

	public ColouredChestConfigGUI(ColouredChestBlockEntity blockEntity, ServerPlayerEntity player) {
		super(ScreenHandlerType.GENERIC_3X3, player, false);
		setTitle(Text.literal("Funny Enderchest"));
		this.blockEntity = blockEntity;
		this.updateFromBlockEntity();
	}

	ItemStack createUpItem(int frequency, String direction) {
		var is = new ItemStack(Items.ARROW);
		is.set(DataComponentTypes.CUSTOM_NAME, Text.literal("Frequency " + frequency + " " + direction).setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.AQUA)));
		return is;
	}

	private ItemStack createFrequencyItem(int frequency, DyeColor dyeColor) {
		var is = new ItemStack(DyeItem.byColor(dyeColor));
		is.set(DataComponentTypes.CUSTOM_NAME, Text.literal("Frequency " + frequency + ": " + dyeColor.getId()).setStyle(Style.EMPTY.withItalic(false).withColor(dyeColor.getSignColor())));
		return is;
	}

	@Override
	public boolean onClick(int index, ClickType type, SlotActionType action, GuiElementInterface element) {
		if (index == 0)
			cycleColour(0, 1);
		else if (index == 6)
			cycleColour(0, -1);
		else if (index == 1)
			cycleColour(1, 1);
		else if (index == 7)
			cycleColour(1, -1);
		else if (index == 2)
			cycleColour(2, 1);
		else if (index == 8)
			cycleColour(2, -1);
		else return super.onClick(index, type, action, element);
		return false;
	}

	void cycleColour(int frequencyIndex, int direction) {
		var newList = new ArrayList<>(blockEntity.extra.frequency());
		newList.set(frequencyIndex, cycleColour(newList.get(frequencyIndex), direction));
		blockEntity.extra = new ColouredChestBlockEntity.ExtraData(Collections.unmodifiableList(newList));
		updateFromBlockEntity();
		blockEntity.markDirty();
	}

	private DyeColor cycleColour(DyeColor dyeColor, int direction) {
		return DyeColor.values()[MathHelper.floorMod(dyeColor.ordinal() + direction, DyeColor.values().length)];
	}

	public void updateFromBlockEntity() {
		setSlot(0, createUpItem(0, "up"));
		setSlot(3, createFrequencyItem(0, blockEntity.extra.frequency().get(0)));
		setSlot(6, createUpItem(0, "down"));

		setSlot(1, createUpItem(1, "up"));
		setSlot(4, createFrequencyItem(1, blockEntity.extra.frequency().get(1)));
		setSlot(7, createUpItem(1, "down"));

		setSlot(2, createUpItem(2, "up"));
		setSlot(5, createFrequencyItem(2, blockEntity.extra.frequency().get(2)));
		setSlot(8, createUpItem(2, "down"));
	}


}
