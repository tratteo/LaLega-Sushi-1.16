package net.la.lega.mod.gui.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.la.lega.mod.gui.handler.PressBlockGUIHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class PressBlockScreen extends CottonInventoryScreen<PressBlockGUIHandler>
{
    public PressBlockScreen(PressBlockGUIHandler container, PlayerEntity player, Text title)
    {
        super(container, player, title);
    }
}