package net.la.lega.mod.gui.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.la.lega.mod.gui.controller.ThreadCutterBlockGUIHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class ThreadCutterBlockScreen extends CottonInventoryScreen<ThreadCutterBlockGUIHandler>
{
    public ThreadCutterBlockScreen(ThreadCutterBlockGUIHandler container, PlayerEntity player, Text title)
    {
        super(container, player, title);
    }
}