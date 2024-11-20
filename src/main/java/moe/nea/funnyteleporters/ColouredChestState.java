package moe.nea.funnyteleporters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class ColouredChestState extends PersistentState {

	public Map<List<DyeColor>, DefaultedList<ItemStack>> inventories = new HashMap<>();

	private static String joinDyeIndex(List<DyeColor> dyes) {
		StringJoiner j = new StringJoiner(";");
		dyes.forEach(it -> j.add(it.getName()));
		return j.toString();
	}

	private static List<DyeColor> splitDyeIndex(String string) {
		List<DyeColor> dyes = new ArrayList<>(3);
		for (String s : string.split(";")) {
			dyes.add(DyeColor.byName(s, DyeColor.BLACK));
		}
		return Collections.unmodifiableList(dyes);
	}

	public static final String ColouredChestInventories = "ColouredChestInventories";

	@Override
	public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		var compound = new NbtCompound();
		inventories.forEach((dyeColors, itemStacks) ->
			                    compound.put(joinDyeIndex(dyeColors), Inventories.writeNbt(new NbtCompound(), itemStacks, registryLookup))
		);
		nbt.put(ColouredChestInventories, compound);
		return nbt;
	}

	public static ColouredChestState createFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		var state = new ColouredChestState();
		var compound = nbt.getCompound(ColouredChestInventories);
		compound.getKeys().forEach(key -> {
			                           var list = DefaultedList.ofSize(27, ItemStack.EMPTY);
			                           Inventories.readNbt(compound.getCompound(key), list, registryLookup);
			                           state.inventories.put(splitDyeIndex(key), list);
		                           }
		);
		return state;
	}

	public DefaultedList<ItemStack> getDelegate(List<DyeColor> dyeColors) {
		return inventories.computeIfAbsent(dyeColors, ignored -> DefaultedList.ofSize(27, ItemStack.EMPTY));
	}

	public static Type<ColouredChestState> TYPE = new Type<>(ColouredChestState::new, ColouredChestState::createFromNbt, null);

	public static Identifier identifier = FunnyTeleporters.id("coloured_chests");

	public static ColouredChestState getServerState(MinecraftServer server) {
		var persistentStateWorld = Objects.requireNonNull(server.getWorld(World.OVERWORLD)).getPersistentStateManager(); // Force all data saving to be done in the overworld
		var state = persistentStateWorld.getOrCreate(TYPE, identifier.toString());
		state.markDirty(); // Always save
		return state;
	}
}
