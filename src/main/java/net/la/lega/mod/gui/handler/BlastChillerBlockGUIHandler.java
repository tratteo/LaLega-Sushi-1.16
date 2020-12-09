package net.la.lega.mod.gui.handler;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.la.lega.mod.entity.abstraction.AProcessingEntity;
import net.la.lega.mod.initializer.LUIControllers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class BlastChillerBlockGUIHandler extends SyncedGuiDescription
{
    public BlastChillerBlockGUIHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context)
    {
        super(LUIControllers.BLASTCHILLER_SCREEN_HANDLER, syncId, playerInventory, getBlockInventory(context, 2), getBlockPropertyDelegate(context, AProcessingEntity.PROPERTY_SIZE));
        
        WPlainPanel root = new WPlainPanel();
        root.setSize(158, 140);
        setRootPanel(root);
        WItemSlot inputSlot = WItemSlot.of(blockInventory, 0);
        WItemSlot outputSlot = WItemSlot.outputOf(blockInventory, 1);

        WBar progressBar = new WBar(new Identifier("lalegamod:textures/ui/progress_bg.png"), new Identifier("lalegamod:textures/ui/progress_bar.png"), AProcessingEntity.PROCESS_TIME, AProcessingEntity.UNIT_PROCESS_TIME, WBar.Direction.RIGHT);
        
        root.add(inputSlot, 34, 32);
        root.add(outputSlot, 110, 32);
        root.add(progressBar, 68, 32, 26, 17);
        root.add(this.createPlayerInventoryPanel(), 0, 70);
        root.validate(this);
    }
}