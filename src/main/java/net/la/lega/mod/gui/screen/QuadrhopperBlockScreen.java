package net.la.lega.mod.gui.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.la.lega.mod.gui.controller.QuadrhopperBlockGUIHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class QuadrhopperBlockScreen extends CottonInventoryScreen<QuadrhopperBlockGUIHandler>
{
    public QuadrhopperBlockScreen(QuadrhopperBlockGUIHandler container, PlayerEntity player, Text title)
    {
        super(container, player, title);
    }
}