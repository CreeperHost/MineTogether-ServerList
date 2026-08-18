package net.creeperhost.minetogetherservers.neoforge;

import net.creeperhost.minetogetherservers.MineTogetherServers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * Created by covers1624 on 20/6/22.
 */
@Mod (MineTogetherServers.MOD_ID)
public class MineTogetherNeoForge {

    public MineTogetherNeoForge(IEventBus eventBus) {
        MineTogetherServers.init();
    }
}
