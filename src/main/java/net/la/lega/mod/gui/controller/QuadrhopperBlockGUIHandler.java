package net.la.lega.mod.gui.controller;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.la.lega.mod.initializer.LUIControllers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class QuadrhopperBlockGUIHandler extends SyncedGuiDescription
{
    
    public QuadrhopperBlockGUIHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context)
    {
        super(LUIControllers.QUADRHOPPER_SCREEN_HANDLER, syncId, playerInventory, getBlockInventory(context, 4), getBlockPropertyDelegate(context));
        WPlainPanel root = new WPlainPanel();
        root.setSize(168, 80);
        setRootPanel(root);
        List<WItemSlot> slots = new ArrayList<>();
        for(int i = 0; i < 4; i++)
        {
            slots.add(WItemSlot.of(blockInventory, i));
        }
        for(int i = 0; i < slots.size(); i++)
        {
            root.add(slots.get(i), 32 + (i * 30), 16);
        }
        root.add(this.createPlayerInventoryPanel(), 4, 48);
        root.validate(this);
    }
}