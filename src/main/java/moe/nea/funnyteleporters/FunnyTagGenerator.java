package moe.nea.funnyteleporters;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class FunnyTagGenerator extends FabricTagProvider.BlockTagProvider {
	public FunnyTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		getTagBuilder(BlockTags.PICKAXE_MINEABLE)
			.add(Registries.BLOCK.getId(FunnyRegistry.COLOURED_CHEST))
			.add(Registries.BLOCK.getId(FunnyRegistry.TELEPORTER))
			.add(Registries.BLOCK.getId(FunnyRegistry.TELEPORTER_NEXUS));
		getTagBuilder(BlockTags.HOE_MINEABLE)
			.add(Registries.BLOCK.getId(FunnyRegistry.ENDER_PEARL_BLOCK));
	}
}
