package moe.nea.funnyteleporters;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
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
			var ref = new Ref(entity);
			if (incomingEntities.remove(ref)) {
				trackedEntities.remove(entity);
				continue;
			}
			if (!trackedEntities.contains(entity)) {
				performTeleport(entity);
			}
		}
		trackedEntities = newEntities;
		while (true) {
			var ref = (Ref) refQueue.poll();
			if (ref == null) break;
			incomingEntities.remove(ref);
		}
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

	class Ref extends WeakReference<Entity> {
		public Ref(Entity referent) {
			super(referent, refQueue);
			hash = System.identityHashCode(referent);
		}

		int hash;

		@Override
		public int hashCode() {
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Ref ref) {
				return ref.hash == this.hash && ref.get() == this.get();
			}
			return false;
		}
	}

	private final ReferenceQueue<Entity> refQueue = new ReferenceQueue<>();
	Set<Entity> trackedEntities = new HashSet<>();
	Set<Ref> incomingEntities = new HashSet<>();
	@Nullable TeleporterDestination destination;

	void performTeleport(Entity subject) {
		if (destination == null) return;
		destination.teleport(subject);
	}

	public void addIncoming(Entity subject) {
		incomingEntities.add(new Ref(subject));
	}
}
