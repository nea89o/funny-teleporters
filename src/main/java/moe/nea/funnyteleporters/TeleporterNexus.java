package moe.nea.funnyteleporters;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TeleporterNexus extends Block implements PolymerBlock, BlockEntityProvider {
	public TeleporterNexus(Settings settings) {
		super(settings);
	}

	@Override
	public @Nullable TeleporterNexusBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TeleporterNexusBlockEntity(pos, state);
	}

	public TeleporterNexusBlockEntity getBE(World world, BlockPos pos) {
		return (TeleporterNexusBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos));
	}

	@Override
	public BlockState getPolymerBlockState(BlockState blockState) {
		return Blocks.ENCHANTING_TABLE.getDefaultState();
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		new TeleporterNexusScreen(getBE(world, pos), (ServerPlayerEntity) player).open();
		return ActionResult.SUCCESS_NO_ITEM_USED;
	}
}
