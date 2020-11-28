package net.la.lega.mod.gui.controller;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.la.lega.mod.entity.SteamCookerBlockEntity;
import net.la.lega.mod.entity.abstraction.AProcessingEntity;
import net.la.lega.mod.initializer.LUIControllers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class SteamCookerBlockGUIHandler extends SyncedGuiDescription
{
    public SteamCookerBlockGUIHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context)
    {
        super(LUIControllers.STEAMCOOKER_SCREEN_HANDLER, syncId, playerInventory, getBlockInventory(context, 3), getBlockPropertyDelegate(context, AProcessingEntity.PROPERTY_SIZE));
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        
        WLabel label = new WLabel("Water level");
        //label.setAlignment(Alignment.CENTER);
        WItemSlot inputSlot = WItemSlot.of(blockInventory, SteamCookerBlockEntity.INPUT_SLOT);
        WItemSlot processingSlot = WItemSlot.of(blockInventory, SteamCookerBlockEntity.PROCESSING_SLOT);
        processingSlot.setModifiable(false);
        WItemSlot outputSlot = WItemSlot.outputOf(blockInventory, SteamCookerBlockEntity.OUTPUT_SLOT);
        WBar progressBar = new WBar(new Identifier("lalegamod:textures/ui/rec_progress_bg.png"), new Identifier("lalegamod:textures/ui/rec_progress_bar.png"), SteamCookerBlockEntity.CURRENT_WATER_LEVEL, SteamCookerBlockEntity.MAX_LEVEL, WBar.Direction.RIGHT);
        WPlayerInvPanel playerInvPanel = this.createPlayerInventoryPanel();
        
        root.add(inputSlot, 20, 36);
        root.add(processingSlot, 72, 34);
        root.add(outputSlot, 130, 36);
        root.add(label, 72, 20);
        root.add(progressBar, 56, 50, 48, 20);
        root.add(playerInvPanel, 0, 70);
        root.validate(this);
    }
}
