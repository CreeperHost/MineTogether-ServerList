package net.creeperhost.minetogetherservers.neoforge;

import net.creeperhost.minetogetherservers.gui.MTTextures;
import net.minecraft.client.resources.model.AtlasManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterTextureAtlasesEvent;

/**
 * Created by brandon3055 on 01/10/2023
 */
public class NeoForgeClientEvents {

    public static void init(IEventBus eventBus) {
        eventBus.addListener(NeoForgeClientEvents::registerTextureAtlas);
    }

    private static void registerTextureAtlas(RegisterTextureAtlasesEvent event) {
        event.register(new AtlasManager.AtlasConfig(MTTextures.TEXTURE_ID, MTTextures.DEFINITION_LOCATION, false));
    }
}
