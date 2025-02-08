package net.creeperhost.minetogetherservers.fabric;

import dev.architectury.platform.Platform;
import net.creeperhost.minetogetherservers.MineTogetherServers;
import net.creeperhost.minetogetherservers.gui.MTTextures;
import net.creeperhost.polylib.fabric.client.ResourceReloadListenerWrapper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

/**
 * Created by covers1624 on 20/6/22.
 */
public class MineTogetherFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        MineTogetherServers.init();

        if (Platform.getEnv() == EnvType.CLIENT) {
            ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new ResourceReloadListenerWrapper(MTTextures::getAtlasHolder, new ResourceLocation(MineTogetherServers.MOD_ID, "gui_atlas_reload")));
        }
    }

}
