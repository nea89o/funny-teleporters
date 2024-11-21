package moe.nea.funnyteleporters;

import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;

public class TeleporterNexusEditorScreen extends SimpleGui {
	private final TeleporterNexusBlockEntity blockEntity;
	private final TeleporterDestination dest;

	public TeleporterNexusEditorScreen(TeleporterNexusBlockEntity blockEntity, TeleporterDestination dest, ServerPlayerEntity player) {
		super(ScreenHandlerType.GENERIC_9X3, player, true);
		this.blockEntity = blockEntity;
		this.dest = dest;
	}

	@Override
	public boolean onAnyClick(int index, ClickType type, SlotActionType action) {

		return super.onAnyClick(index, type, action);
	}
}
