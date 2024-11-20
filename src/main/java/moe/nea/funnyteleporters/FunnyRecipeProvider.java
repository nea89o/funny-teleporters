package moe.nea.funnyteleporters;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class FunnyRecipeProvider extends FabricRecipeProvider {

	public FunnyRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	public void generate(RecipeExporter recipeExporter) {
		ShapelessRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, FunnyRegistry.COLOURED_CHEST)
		                          .input(Items.ENDER_CHEST).input(Items.REDSTONE_BLOCK).input(Items.HOPPER)
		                          .criterion(hasItem(Items.ENDER_CHEST), conditionsFromItem(Items.ENDER_CHEST))
		                          .offerTo(recipeExporter);

	}
}
