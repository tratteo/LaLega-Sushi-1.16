package net.la.lega.mod.loader;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.la.lega.mod.entity_renderer.PressBlockEntityRenderer;
import net.la.lega.mod.gui.handler.*;
import net.la.lega.mod.gui.screen.*;
import net.la.lega.mod.initializer.LBlocks;
import net.la.lega.mod.initializer.LEntities;
import net.la.lega.mod.initializer.LGUIHandlers;
import net.minecraft.client.render.RenderLayer;

public final class ClientLoader implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        initializeScreens();
        setBlocksRenderLayer();
        initializeBlockEntityRenderers();
    }
    
    private void initializeBlockEntityRenderers()
    {
        BlockEntityRendererRegistry.INSTANCE.register(LEntities.PRESS_BLOCK_ENTITY, PressBlockEntityRenderer::new);
    }
    
    private void initializeScreens()
    {
        
        ScreenRegistry.<PressBlockGUIHandler, PressBlockScreen>register(LGUIHandlers.PRESS_SCREEN_HANDLER, (gui, inventory, title) -> new PressBlockScreen(gui, inventory.player, title));
        ScreenRegistry.<BlastChillerBlockGUIHandler, BlastChillerBlockScreen>register(LGUIHandlers.BLASTCHILLER_SCREEN_HANDLER, (gui, inventory, title) -> new BlastChillerBlockScreen(gui, inventory.player, title));
        ScreenRegistry.<FryerBlockGUIHandler, FryerBlockScreen>register(LGUIHandlers.FRYER_SCREEN_HANDLER, (gui, inventory, title) -> new FryerBlockScreen(gui, inventory.player, title));
        ScreenRegistry.<SushiCrafterBlockGUIHandler, SushiCrafterBlockScreen>register(LGUIHandlers.SUSHICRAFTER_SCREEN_HANDLER, (gui, inventory, title) -> new SushiCrafterBlockScreen(gui, inventory.player, title));
        ScreenRegistry.<QuadrhopperBlockGUIHandler, QuadrhopperBlockScreen>register(LGUIHandlers.QUADRHOPPER_SCREEN_HANDLER, (gui, inventory, title) -> new QuadrhopperBlockScreen(gui, inventory.player, title));
        ScreenRegistry.<SteamCookerBlockGUIHandler, SteamCookerBlockScreen>register(LGUIHandlers.STEAMCOOKER_SCREEN_HANDLER, (gui, inventory, title) -> new SteamCookerBlockScreen(gui, inventory.player, title));
        ScreenRegistry.<ThreadCutterBlockGUIHandler, ThreadCutterBlockScreen>register(LGUIHandlers.THREAD_CUTTER_SCREEN_HANDLER, (gui, inventory, title) -> new ThreadCutterBlockScreen(gui, inventory.player, title));
    }
    
    private void setBlocksRenderLayer()
    {
        BlockRenderLayerMap.INSTANCE.putBlock(LBlocks.RICE_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(LBlocks.AVOCADO_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(LBlocks.WASABI_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(LBlocks.STEAM_COOKER_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(LBlocks.PRESS_BLOCK, RenderLayer.getTranslucent());
    }
}