package net.la.lega.mod.gui.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.la.lega.mod.gui.handler.BlastChillerBlockGUIHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class BlastChillerBlockScreen extends CottonInventoryScreen<BlastChillerBlockGUIHandler>
{
    public BlastChillerBlockScreen(BlastChillerBlockGUIHandler container, PlayerEntity player, Text title)
    {
        super(container, player, title);
    }
}