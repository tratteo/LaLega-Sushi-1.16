package net.la.lega.mod.gui.controller;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import net.la.lega.mod.entity.SushiCrafterBlockEntity;
import net.la.lega.mod.entity.abstraction.AProcessingEntity;
import net.la.lega.mod.initializer.LUIControllers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SushiCrafterBlockGUIHandler extends SyncedGuiDescription
{
    public SushiCrafterBlockGUIHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context)
    {
        super(LUIControllers.SUSHICRAFTER_SCREEN_HANDLER, syncId, playerInventory, getBlockInventory(context, 13), getBlockPropertyDelegate(context, AProcessingEntity.PROPERTY_SIZE));
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        
        List<WItemSlot> ingredientSlots = new ArrayList<>();
        for(int i = 0; i < SushiCrafterBlockEntity.INGREDIENTS_SLOTS.length; i++)
        {
            ingredientSlots.add(WItemSlot.of(blockInventory, SushiCrafterBlockEntity.INGREDIENTS_SLOTS[i]));
        }
        List<WItemSlot> requiredSlots = new ArrayList<>();
        for(int i = 0; i < SushiCrafterBlockEntity.REQUIRED_SLOTS.length; i++)
        {
            requiredSlots.add(WItemSlot.of(blockInventory, SushiCrafterBlockEntity.REQUIRED_SLOTS[i]));
        }
        WItemSlot outputSlot = WItemSlot.outputOf(blockInventory, SushiCrafterBlockEntity.OUTPUT_SLOT);
        WBar progressBar = new WBar(new Identifier("lalegamod:textures/ui/progress_bg.png"), new Identifier("lalegamod:textures/ui/progress_bar.png"),
              AProcessingEntity.PROCESS_TIME, AProcessingEntity.UNIT_PROCESS_TIME, WBar.Direction.RIGHT);
        
        WSprite heartIcon = new WSprite(new Identifier("minecraft:textures/item/heart_of_the_sea.png"));
        
        for(int i = 0; i < SushiCrafterBlockEntity.REQUIRED_SLOTS.length; i++)
        {
            root.add(requiredSlots.get(i), 8 + (i * 20), 20);
        }
        
        for(int i = 0; i < SushiCrafterBlockEntity.INGREDIENTS_SLOTS.length; i++)
        {
            int current = i;
            if(i < 4)
            {
                current = i;
            }
            else
            {
                current = i - 4;
            }
            root.add(ingredientSlots.get(i), 8 + (current * 20), (i < 4) ? 62 : 82);
        }
        
        root.add(heartIcon, 37, 40);
        root.add(outputSlot, 132, 48);
        root.add(progressBar, 96, 48, 26, 17);
        root.add(this.createPlayerInventoryPanel(), 0, 112);
        root.validate(this);
    }
}