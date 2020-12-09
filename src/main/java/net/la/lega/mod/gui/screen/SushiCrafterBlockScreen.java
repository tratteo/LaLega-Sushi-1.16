package net.la.lega.mod.gui.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.la.lega.mod.gui.handler.SushiCrafterBlockGUIHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class SushiCrafterBlockScreen extends CottonInventoryScreen<SushiCrafterBlockGUIHandler>
{
    public SushiCrafterBlockScreen(SushiCrafterBlockGUIHandler container, PlayerEntity player, Text title)
    {
        super(container, player, title);
    }
}