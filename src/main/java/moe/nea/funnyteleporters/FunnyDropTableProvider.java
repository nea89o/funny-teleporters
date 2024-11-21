package moe.nea.funnyteleporters;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class FunnyDropTableProvider extends FabricBlockLootTableProvider {
	protected FunnyDropTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generate() {
		addDrop(FunnyRegistry.COLOURED_CHEST);
		addDrop(FunnyRegistry.ENDER_PEARL_BLOCK);
		addDrop(FunnyRegistry.TELEPORTER);
		addDrop(FunnyRegistry.TELEPORTER_NEXUS);
	}
}
