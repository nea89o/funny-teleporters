package moe.nea.funnyteleporters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

public record TeleporterDestination(
	RegistryKey<World> target,
	BlockPos blockPos
) implements Comparable<TeleporterDestination> {
	public static Comparator<TeleporterDestination> COMPARATOR =
		Comparator.<TeleporterDestination, Identifier>comparing(it -> it.target().getValue())
		          .thenComparing(TeleporterDestination::blockPos);

	@Override
	public int compareTo(@NotNull TeleporterDestination o) {
		return COMPARATOR.compare(this, o);
	}

	public void teleport(Entity subject) {
		var sourceWorld = subject.getWorld();
		var destinationWorld = getDestinationWorld((ServerWorld) sourceWorld);
		if (destinationWorld == null) return;
		var destinationBE = destinationWorld.getBlockEntity(blockPos());
		if (!(destinationBE instanceof TeleporterBlockEntity be)) {
			return;
		}
		be.incomingEntities.add(subject);
		var destPos = blockPos().up().toBottomCenterPos();
		subject.teleport(destinationWorld, destPos.x, destPos.y, destPos.z, Set.of(), subject.getYaw(), subject.getPitch());
	}

	public record Labeled(RegistryEntry<Item> item, TeleporterDestination destination, Optional<String> name) {
		public static Codec<Labeled> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Registries.ITEM.getEntryCodec().fieldOf("item").forGetter(Labeled::item),
			TeleporterDestination.CODEC.fieldOf("destination").forGetter(Labeled::destination),
			Codec.STRING.optionalFieldOf("name").forGetter(Labeled::name)
		).apply(instance, Labeled::new));
	}

	public ServerWorld getDestinationWorld(ServerWorld world) {
		return world.getServer().getWorld(target);
	}

	public static Codec<TeleporterDestination> CODEC = RecordCodecBuilder.create(instance -> instance.group(RegistryKey.createCodec(RegistryKeys.WORLD).fieldOf("dim").forGetter(TeleporterDestination::target), BlockPos.CODEC.fieldOf("pos").forGetter(TeleporterDestination::blockPos)).apply(instance, TeleporterDestination::new));
}
