package moe.nea.funnyteleporters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class ColouredChestBlockEntity extends BlockEntity implements Inventory {
	public record ExtraData(
		List<DyeColor> frequency
	) {
		public static Codec<ExtraData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			DyeColor.CODEC.listOf().validate(it -> it.size() == 3 ? DataResult.success(it) : (it.size() > 3 ? DataResult.error(() -> "List " + it + "too long", it.subList(0, 3)) : DataResult.error(() -> "List " + it + " too short"))).fieldOf("frequency").forGetter(ExtraData::frequency)
		).apply(instance, ExtraData::new));
	}

	public ColouredChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		this(FunnyRegistry.COLOURED_CHEST_ENTITY, blockPos, blockState);
	}

	protected ColouredChestBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		var result = ExtraData.CODEC.encodeStart(NbtOps.INSTANCE, extra);
		if (result.hasResultOrPartial()) {
			nbt.put("extra", result.getPartialOrThrow());
		}
	}

	ExtraData extra = new ExtraData(List.of(DyeColor.BLUE, DyeColor.BLUE, DyeColor.BLUE));

	public DefaultedList<ItemStack> getInventory() {
		assert world instanceof ServerWorld;
		return ColouredChestState.getServerState(world.getServer()).getDelegate(extra.frequency());
	}

	public int size() {
		return getInventory().size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemStack : getInventory()) {
			if (!itemStack.isEmpty()) return false;
		}
		return true;
	}

	@Override
	public ItemStack getStack(int slot) {
		if (0 <= slot && slot < size())
			return getInventory().get(slot);
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		ItemStack itemStack = Inventories.splitStack(this.getInventory(), slot, amount);
		if (!itemStack.isEmpty()) {
			markDirty();
		}

		return itemStack;
	}

	@Override
	public ItemStack removeStack(int slot) {
		markDirty();
		return Inventories.removeStack(getInventory(), slot);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		if (0 <= slot && slot < size()) {
			markDirty();
			getInventory().set(slot, stack);
		}
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		if (nbt.contains("extra")) {
			var extra = ExtraData.CODEC.decode(NbtOps.INSTANCE, nbt.get("extra"));
			if (extra.hasResultOrPartial()) {
				this.extra = extra.getPartialOrThrow().getFirst();
			}
		}
	}

	@Override
	public void clear() {
		markDirty();
		getInventory().clear();
	}

	public void openScreen(ServerPlayerEntity player) {
		new ColouredChestConfigGUI(this, player).open();
	}
}

