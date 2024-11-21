package moe.nea.funnyteleporters;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;

import java.util.Iterator;

public class Utils {
	public static ItemStack createBlankBlack() {
		var s = new ItemStack(Items.BLACK_STAINED_GLASS_PANE);
		s.set(DataComponentTypes.CUSTOM_NAME, Text.empty());
		s.set(DataComponentTypes.HIDE_TOOLTIP, Unit.INSTANCE);
		return s;
	}

	public static void skipIt(Iterator<?> it, int skipCount) {
		for (; skipCount > 0 && it.hasNext(); skipCount--) {
			it.next();
		}
	}
}
