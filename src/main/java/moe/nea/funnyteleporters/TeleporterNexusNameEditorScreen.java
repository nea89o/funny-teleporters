package moe.nea.funnyteleporters;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.AnvilInputGui;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.Optional;

public class TeleporterNexusNameEditorScreen extends AnvilInputGui {
	private final TeleporterNexusEditorScreen previous;

	public TeleporterNexusNameEditorScreen(TeleporterNexusEditorScreen previous) {
		super(previous.getPlayer(), false);
		this.previous = previous;
		setTitle(Text.literal("Edit Name"));
		setSlot(2, GuiElementBuilder.from(new ItemStack(Items.OAK_SIGN))
		                            .setName(Text.literal("Click here to save"))
		                            .setCallback(this::save));
		setDefaultInputValue(previous.blockEntity.getName(previous.dest).orElse(""));
	}

	void save() {
		previous.blockEntity.setName(previous.dest, Optional.of(getInput()));
		previous.setSlots();
		previous.open();
	}
}
