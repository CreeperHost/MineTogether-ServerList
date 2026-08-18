package net.creeperhost.minetogetherservers.fabric;

import net.creeperhost.minetogetherservers.MineTogetherServers;
import net.fabricmc.api.ModInitializer;

/**
 * Created by covers1624 on 20/6/22.
 */
public class MineTogetherFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        MineTogetherServers.init();
    }

}
