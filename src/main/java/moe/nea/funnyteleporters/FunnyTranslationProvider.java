package moe.nea.funnyteleporters;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class FunnyTranslationProvider extends FabricLanguageProvider {
	protected FunnyTranslationProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, "en_us", registryLookup);
	}

	@Override
	public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
		translationBuilder.add(FunnyRegistry.ENDER_PEARL_BLOCK, "Block of Ender Pearl");
		translationBuilder.add(FunnyRegistry.TELEPORTER, "Teleporter");
		translationBuilder.add(FunnyRegistry.COLOURED_CHEST, "Colored Chest");
		translationBuilder.add(FunnyRegistry.TELEPORTER_WAND, "Teleporter Wand");
	}
}
