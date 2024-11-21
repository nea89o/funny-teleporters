package moe.nea.funnyteleporters;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TeleporterNexusBlockEntity extends BlockEntity {
	public TeleporterNexusBlockEntity(BlockPos pos, BlockState state) {this(FunnyRegistry.TELEPORTER_NEXUS_ENTITY, pos, state);}

	protected TeleporterNexusBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public TreeMap<TeleporterDestination, RegistryEntry<Item>> destinations = new TreeMap<>();
	private static final Codec<TreeMap<TeleporterDestination, RegistryEntry<Item>>> DESTINATION_CODEC =
		TeleporterDestination.Labeled.CODEC
			.listOf()
			.xmap(
				pairs -> {
					var hash = new TreeMap<TeleporterDestination, RegistryEntry<Item>>();
					for (TeleporterDestination.Labeled pair : pairs) {
						hash.put(pair.destination(), pair.item());
					}
					return hash;
				},
				map -> map.entrySet().stream().map(it -> new TeleporterDestination.Labeled(it.getValue(), it.getKey())).toList()
			);

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		var result = DESTINATION_CODEC.decode(registryLookup.getOps(NbtOps.INSTANCE), nbt.get("destinations"));
		result.resultOrPartial().map(Pair::getFirst).ifPresent(it -> destinations = it);
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		nbt.put("destinations", DESTINATION_CODEC.encodeStart(registryLookup.getOps(NbtOps.INSTANCE), destinations).getPartialOrThrow());
	}

	public void addDestination(TeleporterDestination destination) {
		destinations.putIfAbsent(destination, Registries.ITEM.getEntry(Items.ENDER_PEARL));
		markDirty();
	}

	public void removeDestination(TeleporterDestination destination) {
		destinations.remove(destination);
		markDirty();
	}
}
