package net.la.lega.mod.gui.handler;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WPlayerInvPanel;
import net.la.lega.mod.entity.PressBlockEntity;
import net.la.lega.mod.entity.abstraction.AProcessingEntity;
import net.la.lega.mod.initializer.LGUIHandlers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class PressBlockGUIHandler extends SyncedGuiDescription
{
    PressBlockEntity bufferEntity;
    
    public PressBlockGUIHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context)
    {
        super(LGUIHandlers.PRESS_SCREEN_HANDLER, syncId, playerInventory, getBlockInventory(context, 3), getBlockPropertyDelegate(context, AProcessingEntity.PROPERTY_SIZE));
        
        initializeBufferEntity(context);
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        
        //WDynamicLabel label = new WDynamicLabel(bufferEntity.oilTypeSupplier);
        //label.setAlignment(Alignment.CENTER);
        WItemSlot inputSlot = WItemSlot.of(blockInventory, PressBlockEntity.INPUT_SLOT);
        WItemSlot input2Slot = WItemSlot.of(blockInventory, PressBlockEntity.INPUT2_SLOT);
        WItemSlot outputSlot = WItemSlot.outputOf(blockInventory, PressBlockEntity.OUTPUT_SLOT);
        WBar progressBar = new WBar(new Identifier("lalegamod:textures/ui/press_progress_bg.png"), new Identifier("lalegamod:textures/ui/press_progress_bar.png"), PressBlockEntity.PROCESS_TIME, PressBlockEntity.UNIT_PROCESS_TIME, WBar.Direction.DOWN);
        WPlayerInvPanel playerInvPanel = this.createPlayerInventoryPanel();
        
        root.add(inputSlot, 26, 20);
        root.add(input2Slot, 26, 44);
        root.add(outputSlot, 130, 36);
        //root.add(label, 72, 20);
        root.add(progressBar, 68, 16, 30, 40);
        root.add(playerInvPanel, 0, 70);
        root.validate(this);
    }
    
    private void initializeBufferEntity(ScreenHandlerContext context)
    {
        PressBlockEntity[] lambdaBypass = new PressBlockEntity[1];
        
        context.run((world, blockPosition) ->
        {
            PressBlockEntity temporaryEntity = (PressBlockEntity) world.getBlockEntity(blockPosition);
            lambdaBypass[0] = temporaryEntity;
        });
        bufferEntity = lambdaBypass[0];
    }
}
