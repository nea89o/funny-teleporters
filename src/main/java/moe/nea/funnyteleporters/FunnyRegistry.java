package moe.nea.funnyteleporters;

import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FunnyRegistry {
	public static Block COLOURED_CHEST = registerBlock("coloured_chest", new ColouredChestBlock(AbstractBlock.Settings.create()), Items.RED_WOOL);
	public static BlockEntityType<ColouredChestBlockEntity> COLOURED_CHEST_ENTITY = registerBlockEntity("coloured_chest", BlockEntityType.Builder.create(ColouredChestBlockEntity::new, COLOURED_CHEST));


	private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, BlockEntityType.Builder<T> builder, Block... blocks) {
		var be = Registry.register(Registries.BLOCK_ENTITY_TYPE,
		                           Identifier.of(FunnyTeleporters.MOD_ID, name),
		                           builder.build()
		);
		PolymerBlockUtils.registerBlockEntity(be);
		return be;
	}

	private static <T extends Block> T registerBlock(String name, T block, Item blockItem) {
		var id = Identifier.of(FunnyTeleporters.MOD_ID, name);
		Registry.register(Registries.ITEM, id, new PolymerBlockItem(block, new Item.Settings(), blockItem));
		return Registry.register(Registries.BLOCK, id, block);
	}

	public static void init() {

	}
}
