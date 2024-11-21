package moe.nea.funnyteleporters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record TeleporterDestination(
	RegistryKey<World> target,
	BlockPos blockPos
) {
	public ServerWorld getDestinationWorld(ServerWorld world) {
		return world.getServer().getWorld(target);
	}

	public static Codec<TeleporterDestination> CODEC = RecordCodecBuilder.create(instance -> instance.group(RegistryKey.createCodec(RegistryKeys.WORLD).fieldOf("dim").forGetter(TeleporterDestination::target), BlockPos.CODEC.fieldOf("pos").forGetter(TeleporterDestination::blockPos)).apply(instance, TeleporterDestination::new));
}
