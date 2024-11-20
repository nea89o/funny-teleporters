package moe.nea.funnyteleporters;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunnyTeleporters implements ModInitializer {
	public static final String MOD_ID = "funny-teleporters";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		FunnyRegistry.init();
	}
}