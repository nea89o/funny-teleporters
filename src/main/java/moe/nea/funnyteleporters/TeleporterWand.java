package moe.nea.funnyteleporters;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.Nullable;

public class TeleporterWand extends Item implements PolymerItem {
	public TeleporterWand(Settings settings) {
		super(settings);
	}

	@Override
	public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity serverPlayerEntity) {
		return Items.CYAN_BANNER;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() == FunnyRegistry.TELEPORTER) {
			if (context.getPlayer() != null && context.getPlayer().isSneaking()) {
				context.getStack().set(FunnyRegistry.TELEPORTER_DESTINATION, new TeleporterDestination(context.getWorld().getRegistryKey(), context.getBlockPos()));
				context.getPlayer().sendMessage(Text.literal("Saved teleport destination to wand."));
			} else {
				var be = FunnyRegistry.TELEPORTER.getBE(context.getWorld(), context.getBlockPos());
				// TODO: check for empty destination
				be.destination = context.getStack().get(FunnyRegistry.TELEPORTER_DESTINATION);
				be.markDirty();
				if (context.getPlayer() != null) {
					context.getPlayer().sendMessage(Text.literal("Set new target destination to wand."));
				}
			}
			return ActionResult.SUCCESS_NO_ITEM_USED;
		}
		return super.useOnBlock(context);
	}
}
