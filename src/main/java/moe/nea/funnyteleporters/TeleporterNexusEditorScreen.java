package moe.nea.funnyteleporters;

import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TeleporterNexusEditorScreen extends SimpleGui {
	final TeleporterNexusBlockEntity blockEntity;
	final TeleporterDestination dest;

	public TeleporterNexusEditorScreen(TeleporterNexusBlockEntity blockEntity, TeleporterDestination dest, ServerPlayerEntity player) {
		super(ScreenHandlerType.GENERIC_9X3, player, false);
		this.blockEntity = blockEntity;
		this.dest = dest;
		setSlots();
	}

	void setSlots() {
		setTitle(Text.literal("Set Icon for " + blockEntity.getName(dest).orElse("teleporter")));
		for (int i = 0; i < width * height; i++) {
			setSlot(i, Utils.createBlankBlack());
		}
		setSlot(4 + 9,
		        GuiElementBuilder.from(new ItemStack(blockEntity.getIcon(dest)))
		                         .setName(Text.literal("Click item in your inventory to set icon.")));
		setSlot(2 + 9,
		        GuiElementBuilder.from(new ItemStack(Items.OAK_SIGN))
		                         .setName(Text.literal("Click here to change name"))
		                         .addLoreLine(Text.literal("Click here to set a name for the teleport destination")
		                                          .setStyle(Utils.colouredLoreStyle(Formatting.AQUA)))
		                         .setCallback(this::handleNameChange));
		setSlot(9 + 6,
		        GuiElementBuilder.from(new ItemStack(Items.BARRIER))
		                         .setName(Text.literal("Delete").withColor(0xFFFF0000))
		                         .addLoreLine(Text.literal("Shift-Click here to remove this location")
		                                          .setStyle(Utils.colouredLoreStyle(Formatting.RED)))
		                         .addLoreLine(Text.literal("from the teleport nexus")
		                                          .setStyle(Utils.colouredLoreStyle(Formatting.RED)))
		                         .setCallback(this::handleDelete));
	}

	void handleNameChange() {
		new TeleporterNexusNameEditorScreen(this).open();
	}

	void handleDelete(ClickType clickType) {
		if (clickType == ClickType.MOUSE_LEFT_SHIFT) {
			blockEntity.removeDestination(dest);
			back();
		} else {
			player.sendMessage(Text.literal("You need to shift click to delete locations."));
		}
	}

	@Override
	public void onClose() {
		back();
	}

	void back() {
		new TeleporterNexusScreen(blockEntity, player).open();
	}

	@Override
	public boolean onAnyClick(int index, ClickType type, SlotActionType action) {
		var slot = getSlotRedirectOrPlayer(index);
		if (slot == null) return true;
		var stack = slot.getStack();
		if (!stack.isEmpty()) {
			blockEntity.setIcon(dest, stack.getItem());
			setSlots();
			if (screenHandler != null)
				screenHandler.syncState();
		}
		return false;
	}
}
