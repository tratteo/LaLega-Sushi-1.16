package net.la.lega.mod.initializer;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.la.lega.mod.block.*;
import net.la.lega.mod.gui.controller.*;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;

public final class LUIControllers
{
    public static ScreenHandlerType THREAD_CUTTER_SCREEN_HANDLER;
    public static ScreenHandlerType PRESS_SCREEN_HANDLER;
    public static ScreenHandlerType BLASTCHILLER_SCREEN_HANDLER;
    public static ScreenHandlerType SUSHICRAFTER_SCREEN_HANDLER;
    public static ScreenHandlerType FRYER_SCREEN_HANDLER;
    public static ScreenHandlerType STEAMCOOKER_SCREEN_HANDLER;
    public static ScreenHandlerType QUADRHOPPER_SCREEN_HANDLER;
    
    public static void initialize()
    {
        //ContainerProviderRegistry.INSTANCE.registerFactory(BlastChillerBlock.ID, (syncId, id, player, buf) -> new BlastChillerBlockGUIHandler(syncId, player.inventory, ScreenHandlerContext.create(player.world, buf.readBlockPos())));
        //ContainerProviderRegistry.INSTANCE.registerFactory(ThreadCutterBlock.ID, (syncId, id, player, buf) -> new ThreadCutterBlockGUIHandler(syncId, player.inventory, ScreenHandlerContext.create(player.world, buf.readBlockPos())));
        //ContainerProviderRegistry.INSTANCE.registerFactory(SushiCrafterBlock.ID, (syncId, id, player, buf) -> new SushiCrafterBlockGUIHandler(syncId, player.inventory, ScreenHandlerContext.create(player.world, buf.readBlockPos())));
        //ContainerProviderRegistry.INSTANCE.registerFactory(FryerBlock.ID, (syncId, id, player, buf) -> new FryerBlockGUIHandler(syncId, player.inventory, ScreenHandlerContext.create(player.world, buf.readBlockPos())));
        //ContainerProviderRegistry.INSTANCE.registerFactory(SteamCookerBlock.ID, (syncId, id, player, buf) -> new SteamCookerBlockGUIHandler(syncId, player.inventory, ScreenHandlerContext.create(player.world, buf.readBlockPos())));
        //ContainerProviderRegistry.INSTANCE.registerFactory(PressBlock.ID, (syncId, id, player, buf) -> new PressBlockGUIHandler(syncId, player.inventory, ScreenHandlerContext.create(player.world, buf.readBlockPos())));
        //ContainerProviderRegistry.INSTANCE.registerFactory(QuadrhopperBlock.ID, (syncId, id, player, buf) -> new QuadrhopperBlockGUIHandler(syncId, player.inventory, ScreenHandlerContext.create(player.world, buf.readBlockPos())));
        
        THREAD_CUTTER_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(ThreadCutterBlock.ID, (syncId, inventory, buf) -> new ThreadCutterBlockGUIHandler(syncId, inventory, ScreenHandlerContext.create(inventory.player.world, buf.readBlockPos())));
        PRESS_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(PressBlock.ID, (syncId, inventory, buf) -> new PressBlockGUIHandler(syncId, inventory, ScreenHandlerContext.create(inventory.player.world, buf.readBlockPos())));
        BLASTCHILLER_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(BlastChillerBlock.ID, (syncId, inventory, buf) -> new BlastChillerBlockGUIHandler(syncId, inventory, ScreenHandlerContext.create(inventory.player.world, buf.readBlockPos())));
        SUSHICRAFTER_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(SushiCrafterBlock.ID, (syncId, inventory, buf) -> new SushiCrafterBlockGUIHandler(syncId, inventory, ScreenHandlerContext.create(inventory.player.world, buf.readBlockPos())));
        FRYER_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(FryerBlock.ID, (syncId, inventory, buf) -> new FryerBlockGUIHandler(syncId, inventory, ScreenHandlerContext.create(inventory.player.world, buf.readBlockPos())));
        STEAMCOOKER_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(SteamCookerBlock.ID, (syncId, inventory, buf) -> new SteamCookerBlockGUIHandler(syncId, inventory, ScreenHandlerContext.create(inventory.player.world, buf.readBlockPos())));
        QUADRHOPPER_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(QuadrhopperBlock.ID, (syncId, inventory, buf) -> new QuadrhopperBlockGUIHandler(syncId, inventory, ScreenHandlerContext.create(inventory.player.world, buf.readBlockPos())));
    }
}