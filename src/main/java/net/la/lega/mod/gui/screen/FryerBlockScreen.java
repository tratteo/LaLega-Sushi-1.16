package net.la.lega.mod.gui.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.la.lega.mod.gui.handler.FryerBlockGUIHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class FryerBlockScreen extends CottonInventoryScreen<FryerBlockGUIHandler>
{
    public FryerBlockScreen(FryerBlockGUIHandler container, PlayerEntity player, Text title)
    {
        super(container, player, title);
    }
}