package net.creeperhost.minetogetherservers.neoforge;

import net.creeperhost.minetogetherservers.MineTogetherServers;
import net.creeperhost.minetogetherservers.chat.MineTogetherChat;
import net.creeperhost.minetogetherservers.orderform.OrderForm;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Created by covers1624 on 20/6/22.
 */
@Mod (MineTogetherServers.MOD_ID)
public class MineTogetherNeoForge {

    public MineTogetherNeoForge(IEventBus eventBus) {
        MineTogetherServers.init();

        if (FMLEnvironment.dist.isClient()) {
            NeoForge.EVENT_BUS.addListener(this::clientInit);
            NeoForgeClientEvents.init(eventBus);
        }
    }

    private void clientInit(ScreenEvent.Init.Post event) {
        //We need this because INIT_POST from architectury only works in the initial init event.
        //It does not fire on re-init, e.g. when window is resized. I would consider this a bug in architectury.
        OrderForm.onScreenPostInit(event.getScreen());
        MineTogetherChat.onScreenPostInit(event.getScreen());
    }
}
