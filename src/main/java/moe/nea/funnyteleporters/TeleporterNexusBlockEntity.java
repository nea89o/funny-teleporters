package moe.nea.funnyteleporters;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;
import java.util.TreeMap;

public class TeleporterNexusBlockEntity extends BlockEntity {
	public TeleporterNexusBlockEntity(BlockPos pos, BlockState state) {this(FunnyRegistry.TELEPORTER_NEXUS_ENTITY, pos, state);}

	protected TeleporterNexusBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public Optional<String> getName(TeleporterDestination dest) {
		var label = destinations.get(dest);
		return label != null ? (label.label) : Optional.empty();
	}

	public ItemConvertible getIcon(TeleporterDestination dest) {
		var label = destinations.get(dest);
		return (label != null ? label.item : getDefaultItem()).value();
	}

	public record Label(
		RegistryEntry<Item> item,
		Optional<String> label
	) {
	}

	public TreeMap<TeleporterDestination, Label> destinations = new TreeMap<>();
	private static final Codec<TreeMap<TeleporterDestination, Label>> DESTINATION_CODEC =
		TeleporterDestination.Labeled.CODEC
			.listOf()
			.xmap(
				pairs -> {
					var hash = new TreeMap<TeleporterDestination, Label>();
					for (TeleporterDestination.Labeled pair : pairs) {
						hash.put(pair.destination(), new Label(pair.item(), pair.name()));
					}
					return hash;
				},
				map -> map.entrySet().stream().map(it -> new TeleporterDestination.Labeled(it.getValue().item(), it.getKey(), it.getValue().label())).toList()
			);

	static RegistryEntry<Item> getDefaultItem() {
		return Registries.ITEM.getEntry(Items.ENDER_PEARL);
	}

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
		destinations.putIfAbsent(destination, new Label(getDefaultItem(), Optional.empty()));
		markDirty();
	}

	public void removeDestination(TeleporterDestination destination) {
		destinations.remove(destination);
		markDirty();
	}

	public void setIcon(TeleporterDestination dest, Item item) {
		var entry = Registries.ITEM.getEntry(item);
		destinations.compute(dest, (key, old) -> old == null ? new Label(entry, Optional.empty()) : new Label(entry, old.label()));
		markDirty();
	}

	public void setName(TeleporterDestination dest, Optional<String> string) {
		destinations.compute(dest, (key, old) -> old == null ? new Label(getDefaultItem(), string) : new Label(old.item(), string));
		markDirty();
	}
}
