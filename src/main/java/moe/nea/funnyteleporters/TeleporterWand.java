package moe.nea.funnyteleporters;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeleporterWand extends Item implements PolymerItem {
	public TeleporterWand(Settings settings) {
		super(settings);
	}

	@Override
	public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity serverPlayerEntity) {
		return Items.CYAN_BANNER;
	}

	@Override
	public ItemStack getPolymerItemStack(ItemStack original, TooltipType tooltipType, RegistryWrapper.WrapperLookup lookup, @Nullable ServerPlayerEntity player) {
		var stack = PolymerItem.super.getPolymerItemStack(original, tooltipType, lookup, player);
		var dest = original.get(FunnyRegistry.TELEPORTER_DESTINATION);
		if (dest != null) {
			stack.set(DataComponentTypes.LORE, new LoreComponent(dest.formatName("Linked to")));
		}
		return stack;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		var stack = user.getStackInHand(hand);
		if (user.isSneaking()) {
			if (stack.remove(FunnyRegistry.TELEPORTER_DESTINATION) != null) {
				user.sendMessage(Text.literal("Cleared saved destination."));
			}
		}
		return TypedActionResult.success(stack, world.isClient());
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		var destination = context.getStack().get(FunnyRegistry.TELEPORTER_DESTINATION);
		var block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();
		if (block == FunnyRegistry.TELEPORTER_NEXUS && destination != null) {
			var nexus = FunnyRegistry.TELEPORTER_NEXUS.getBE(context.getWorld(), context.getBlockPos());
			if (context.getPlayer() != null)
				context.getPlayer().sendMessage(Text.literal("Saved destination from wand into nexus."));
			nexus.addDestination(destination);
		}
		if (block == FunnyRegistry.TELEPORTER) {
			if (context.getPlayer() != null && context.getPlayer().isSneaking()) {
				context.getStack().set(FunnyRegistry.TELEPORTER_DESTINATION, new TeleporterDestination(context.getWorld().getRegistryKey(), context.getBlockPos()));
				context.getPlayer().sendMessage(Text.literal("Saved teleport destination to wand."));
			} else if (destination != null){
				var be = FunnyRegistry.TELEPORTER.getBE(context.getWorld(), context.getBlockPos());
				be.destination = destination;
				be.markDirty();
				if (context.getPlayer() != null) {
					context.getPlayer().sendMessage(Text.literal("Set new target destination from wand."));
				}
			}
			return ActionResult.SUCCESS_NO_ITEM_USED;
		}
		return super.useOnBlock(context);
	}
}
