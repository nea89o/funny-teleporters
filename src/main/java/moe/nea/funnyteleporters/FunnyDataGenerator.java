package moe.nea.funnyteleporters;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class FunnyDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		var pack = dataGenerator.createPack();
		pack.addProvider(FunnyRecipeProvider::new);
		pack.addProvider(FunnyDropTableProvider::new);
		pack.addProvider(FunnyTagGenerator::new);
	}

}
