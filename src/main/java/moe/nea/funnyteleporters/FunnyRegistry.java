package moe.nea.funnyteleporters;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FunnyRegistry {
	public static final String MODID = "funny_teleporters";
	public static Block COLOURED_CHEST = registerBlock("coloured_chest", new ColouredChestBlock(AbstractBlock.Settings.create()));
	public static BlockEntityType<ColouredChestBlockEntity> COLOURED_CHEST_ENTITY = registerBlockEntity("coloured_chest", BlockEntityType.Builder.create(ColouredChestBlockEntity::new, COLOURED_CHEST));


	private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, BlockEntityType.Builder<T> builder, Block... blocks) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE,
		                         Identifier.of(MODID, name),
		                         builder.build()
		);
	}

	private static <T extends Block> T registerBlock(String name, T block) {
		var id = Identifier.of(MODID, name);
		Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
		return Registry.register(Registries.BLOCK, id, block);
	}

	public static void init() {

	}
}
