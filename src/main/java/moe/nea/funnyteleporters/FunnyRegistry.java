package moe.nea.funnyteleporters;

import com.mojang.serialization.Codec;
import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import eu.pb4.polymer.core.api.block.SimplePolymerBlock;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import eu.pb4.polymer.rsm.api.RegistrySyncUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FunnyRegistry {
	public static Block COLOURED_CHEST = registerBlock("coloured_chest", new ColouredChestBlock(AbstractBlock.Settings.create()
	                                                                                                                  .pistonBehavior(PistonBehavior.PUSH_ONLY)
	                                                                                                                  .strength(50.0F, 1200.0F)), Items.REINFORCED_DEEPSLATE);
	public static Block ENDER_PEARL_BLOCK = registerBlock("ender_pearl_block", new SimplePolymerBlock(AbstractBlock.Settings.create()
	                                                                                                                        .strength(10F), Blocks.GREEN_WOOL), Items.GREEN_WOOL);

	public static TeleporterBlock TELEPORTER = registerBlock("teleporter", new TeleporterBlock(AbstractBlock.Settings.create()
	                                                                                                                 .pistonBehavior(PistonBehavior.BLOCK)
	                                                                                                                 .strength(50F, 1200F)), Items.SEA_LANTERN);

	public static TeleporterNexus TELEPORTER_NEXUS = registerBlock("teleporter_nexus", new TeleporterNexus(AbstractBlock.Settings.create().pistonBehavior(PistonBehavior.BLOCK).strength(100F, 1200F)), Items.ENCHANTING_TABLE);

	public static ComponentType<TeleporterDestination> TELEPORTER_DESTINATION = registerComponentType("teleporter_destination", TeleporterDestination.CODEC);


	public static BlockEntityType<ColouredChestBlockEntity> COLOURED_CHEST_ENTITY = registerBlockEntity("coloured_chest", BlockEntityType.Builder.create(ColouredChestBlockEntity::new, COLOURED_CHEST));
	public static BlockEntityType<TeleporterBlockEntity> TELEPORTER_ENTITY = registerBlockEntity("teleporter", BlockEntityType.Builder.create(TeleporterBlockEntity::new, TELEPORTER));
	public static TeleporterWand TELEPORTER_WAND = registerItem("teleporter_wand", new TeleporterWand(new Item.Settings().maxCount(1)));
	public static BlockEntityType<TeleporterNexusBlockEntity> TELEPORTER_NEXUS_ENTITY = registerBlockEntity("teleporter_nexus", BlockEntityType.Builder.create(TeleporterNexusBlockEntity::new, TELEPORTER_NEXUS));

	private static <T> ComponentType<T> registerComponentType(String name, Codec<T> codec) {
		var comp = Registry.register(Registries.DATA_COMPONENT_TYPE, FunnyTeleporters.id(name), ComponentType.<T>builder().codec(codec).build());
		RegistrySyncUtils.setServerEntry(Registries.DATA_COMPONENT_TYPE, comp);
		return comp;
	}

	private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, BlockEntityType.Builder<T> builder) {
		var be = Registry.register(Registries.BLOCK_ENTITY_TYPE,
		                           Identifier.of(FunnyTeleporters.MOD_ID, name),
		                           builder.build()
		);
		PolymerBlockUtils.registerBlockEntity(be);
		return be;
	}

	private static <T extends Item> T registerItem(String name, T item) {
		return Registry.register(Registries.ITEM, FunnyTeleporters.id(name), item);
	}

	private static <T extends Block> T registerBlock(String name, T block, Item blockItem) {
		var id = Identifier.of(FunnyTeleporters.MOD_ID, name);
		Registry.register(Registries.ITEM, id, new PolymerBlockItem(block, new Item.Settings(), blockItem));
		return Registry.register(Registries.BLOCK, id, block);
	}

	public static void init() {

	}
}
