package moe.nea.funnyteleporters;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
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

		ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, FunnyRegistry.ENDER_PEARL_BLOCK)
		                       .pattern("eee").pattern("eee").pattern("eee")
		                       .input('e', Items.ENDER_PEARL)
		                       .criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL))
		                       .offerTo(recipeExporter);

		ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.ENDER_PEARL, 9)
		                          .input(FunnyRegistry.ENDER_PEARL_BLOCK)
		                          .criterion(hasItem(FunnyRegistry.ENDER_PEARL_BLOCK), conditionsFromItem(FunnyRegistry.ENDER_PEARL_BLOCK))
		                          .offerTo(recipeExporter);
		ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, FunnyRegistry.TELEPORTER)
		                       .pattern("ooo")
		                       .pattern("oeo")
		                       .pattern("ooo")
		                       .input('o', Items.OBSIDIAN)
		                       .input('e', FunnyRegistry.ENDER_PEARL_BLOCK)
		                       .criterion(hasItem(FunnyRegistry.ENDER_PEARL_BLOCK), conditionsFromItem(FunnyRegistry.ENDER_PEARL_BLOCK))
		                       .offerTo(recipeExporter);
		ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, FunnyRegistry.TELEPORTER_WAND)
		                       .pattern("  e")
		                       .pattern(" r ")
		                       .pattern("s  ")
		                       .input('e', FunnyRegistry.ENDER_PEARL_BLOCK)
		                       .input('r', Items.REDSTONE_BLOCK)
		                       .input('s', Items.STICK)
		                       .criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL))
		                       .offerTo(recipeExporter);
	}
}
