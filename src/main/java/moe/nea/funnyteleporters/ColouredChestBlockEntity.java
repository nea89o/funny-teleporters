package moe.nea.funnyteleporters;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.math.BlockPos;

public class ColouredChestBlockEntity extends ChestBlockEntity {
	public ColouredChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		this(FunnyRegistry.COLOURED_CHEST_ENTITY, blockPos, blockState);
	}

	protected ColouredChestBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}
}
