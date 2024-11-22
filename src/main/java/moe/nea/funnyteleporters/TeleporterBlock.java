package moe.nea.funnyteleporters;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TeleporterBlock extends Block implements PolymerBlock, BlockEntityProvider {
	public TeleporterBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	public BlockState getPolymerBlockState(BlockState blockState) {
		return Blocks.RESPAWN_ANCHOR.getDefaultState().with(RespawnAnchorBlock.CHARGES, RespawnAnchorBlock.MAX_CHARGES);
	}

	public TeleporterBlockEntity getBE(World world, BlockPos pos) {
		return ((TeleporterBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos)));
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return TeleporterBlockEntity::tick;
	}

	@Override
	public TeleporterBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TeleporterBlockEntity(pos, state);
	}
}
