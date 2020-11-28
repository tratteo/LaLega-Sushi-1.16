package net.la.lega.mod.gui.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.la.lega.mod.gui.controller.SteamCookerBlockGUIHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class SteamCookerBlockScreen extends CottonInventoryScreen<SteamCookerBlockGUIHandler>
{
    public SteamCookerBlockScreen(SteamCookerBlockGUIHandler container, PlayerEntity player, Text title)
    {
        super(container, player, title);
    }
}