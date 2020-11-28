package net.la.lega.mod.gui.controller;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.la.lega.mod.entity.abstraction.AProcessingEntity;
import net.la.lega.mod.initializer.LUIControllers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class ThreadCutterBlockGUIHandler extends SyncedGuiDescription
{
    public ThreadCutterBlockGUIHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context)
    {
        super(LUIControllers.THREAD_CUTTER_SCREEN_HANDLER, syncId, playerInventory, getBlockInventory(context, 1), getBlockPropertyDelegate(context, AProcessingEntity.PROPERTY_SIZE));
        
        WPlainPanel root = new WPlainPanel();
        root.setSize(160, 65);
        
        setRootPanel(root);
        WItemSlot inputSlot = WItemSlot.outputOf(blockInventory, 0);
        WBar progressBar = new WBar(new Identifier("lalegamod:textures/ui/progress_knife_bg.png"), new Identifier("lalegamod:textures/ui/progress_knife_bar.png"), AProcessingEntity.PROCESS_TIME, AProcessingEntity.UNIT_PROCESS_TIME, WBar.Direction.RIGHT);
        root.add(progressBar, 100, 31);
        root.add(inputSlot, 72, 32);
        
        root.add(this.createPlayerInventoryPanel(), 0, 70);
        root.validate(this);
    }
}