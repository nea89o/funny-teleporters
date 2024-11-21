package moe.nea.funnyteleporters;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class TeleporterBlockEntity extends BlockEntity {
	public TeleporterBlockEntity(BlockPos pos, BlockState state) {
		this(FunnyRegistry.TELEPORTER_ENTITY, pos, state);
	}

	protected TeleporterBlockEntity(BlockEntityType<? extends TeleporterBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public static <T extends BlockEntity> void tick(World world, BlockPos blockPos, BlockState blockState, T t) {
		((TeleporterBlockEntity) t).doTick(world, blockPos, blockState);
	}

	private void doTick(World world, BlockPos blockPos, BlockState blockState) {
		var up = blockPos.up();
		var newEntities = new HashSet<>(world.getEntitiesByType(TypeFilter.instanceOf(Entity.class), Box.enclosing(up, up), entity -> true));
		for (Entity entity : newEntities) {
			if (incomingEntities.remove(entity)) {
				trackedEntities.remove(entity);
				continue;
			}
			if (!trackedEntities.contains(entity)) {
				performTeleport(entity);
			}
		}
		trackedEntities = newEntities;
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		if (destination != null) {
			nbt.put("destination", TeleporterDestination.CODEC.encodeStart(NbtOps.INSTANCE, destination).getPartialOrThrow());
		}
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		if (nbt.contains("destination")) {
			var result = TeleporterDestination.CODEC.decode(NbtOps.INSTANCE, nbt.getCompound("destination"));
			if (result.hasResultOrPartial()) {
				destination = result.getPartialOrThrow().getFirst();
			}
		}
	}

	Set<Entity> trackedEntities = new HashSet<>();
	Set<Entity> incomingEntities = new HashSet<>();
	@Nullable TeleporterDestination destination;

	void performTeleport(Entity subject) {
		if (destination == null) return;
		var sourceWorld = subject.getWorld();
		var destinationWorld = destination.getDestinationWorld((ServerWorld) sourceWorld);
		if (destinationWorld == null) return;
		var destinationBE = destinationWorld.getBlockEntity(destination.blockPos());
		if (!(destinationBE instanceof TeleporterBlockEntity be)) {
			return;
		}
		be.incomingEntities.add(subject);
		var destPos = destination.blockPos().up().toBottomCenterPos();
		subject.teleport(destinationWorld, destPos.x, destPos.y, destPos.z, Set.of(), subject.getYaw(), subject.getPitch());
	}
}
