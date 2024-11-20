package moe.nea.funnyteleporters;

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
import net.minecraft.util.Unit;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Collections;

public class ColouredChestConfigGUI extends SimpleGui {

	private final ColouredChestBlockEntity blockEntity;

	public ColouredChestConfigGUI(ColouredChestBlockEntity blockEntity, ServerPlayerEntity player) {
		super(ScreenHandlerType.GENERIC_9X3, player, false);
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
		int x = index % width;
		int y = index / width;
		if (x == 0 || x == 1 || x == 2) {
			cycleColour(x, y == 0 ? -1 : y == 2 ? 1 : 0);
		}
		if (index == width + 5) {
			new ColouredChestViewScreen(player, blockEntity).open();
		}
		return super.onClick(index, type, action, element);
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
		for (int i = 0; i < 3; i++) {
			setSlot(i, createUpItem(i, "up"));
			setSlot(i + width, createFrequencyItem(i, blockEntity.extra.frequency().get(i)));
			setSlot(i + 2 * width, createUpItem(i, "down"));
		}
		for (int x = 3; x < 9; x++) {
			for (int y = 0; y < 3; y++) {
				var s = new ItemStack(Items.BLACK_STAINED_GLASS_PANE);
				s.set(DataComponentTypes.CUSTOM_NAME, Text.empty());
				s.set(DataComponentTypes.HIDE_TOOLTIP, Unit.INSTANCE);
				setSlot(y * width + x, s);
			}
		}
		{
			var s = new ItemStack(Items.CHEST);
			s.set(DataComponentTypes.CUSTOM_NAME, Text.literal("View chest").setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.DARK_RED)));
			setSlot(width + 5, s);
		}
	}
}
