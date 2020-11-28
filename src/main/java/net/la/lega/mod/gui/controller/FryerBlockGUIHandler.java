package net.la.lega.mod.gui.controller;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.la.lega.mod.entity.FryerBlockEntity;
import net.la.lega.mod.initializer.LUIControllers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class FryerBlockGUIHandler extends SyncedGuiDescription
{
    FryerBlockEntity entity;
    
    public FryerBlockGUIHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context)
    {
        super(LUIControllers.FRYER_SCREEN_HANDLER, syncId, playerInventory, getBlockInventory(context, 3), getBlockPropertyDelegate(context, 2));
        
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        context.run((world, pos) ->
        {
            entity = (FryerBlockEntity) world.getBlockEntity(pos);
        });
        WDynamicLabel label = new WDynamicLabel(entity.oilTypeSupplier);
        label.setAlignment(HorizontalAlignment.CENTER);
        
        WItemSlot inputSlot = WItemSlot.of(blockInventory, FryerBlockEntity.INPUT_SLOT);
        WItemSlot processingSlot = WItemSlot.of(blockInventory, FryerBlockEntity.PROCESSING_SLOT);
        processingSlot.setModifiable(false);
        WItemSlot outputSlot = WItemSlot.outputOf(blockInventory, FryerBlockEntity.OUTPUT_SLOT);
        WBar progressBar = new WBar(new Identifier("lalegamod:textures/ui/rec_progress_bg.png"), new Identifier("lalegamod:textures/ui/rec_progress_bar.png"), FryerBlockEntity.CURRENT_OIL_USAGE, FryerBlockEntity.MAX_USAGE, WBar.Direction.RIGHT);
        WPlayerInvPanel playerInvPanel = this.createPlayerInventoryPanel();
        
        //root.add(label, 72, 20);
        root.add(inputSlot, 20, 36);
        root.add(processingSlot, 72, 34);
        root.add(outputSlot, 130, 36);
        root.add(progressBar, 56, 50, 48, 20);
        root.add(playerInvPanel, 0, 70);
        root.validate(this);
    }
}
