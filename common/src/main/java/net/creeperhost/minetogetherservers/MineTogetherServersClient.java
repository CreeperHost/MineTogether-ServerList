package net.creeperhost.minetogetherservers;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.hooks.client.screen.ScreenAccess;
import net.creeperhost.minetogetherservers.chat.FriendChatNotifier;
import net.creeperhost.minetogetherservers.chat.MineTogetherChat;
import net.creeperhost.minetogetherservers.chat.gui.ChatScreenInjection;
import net.creeperhost.minetogetherservers.config.Config;
import net.creeperhost.minetogetherservers.connect.MineTogetherConnect;
import net.creeperhost.minetogetherservers.gui.SettingGui;
import net.creeperhost.minetogether.lib.web.ApiClientResponse;
import net.creeperhost.minetogetherservers.serverlist.MineTogetherServerList;
import net.creeperhost.minetogetherservers.serverlist.data.Server;
import net.creeperhost.minetogetherservers.serverlist.web.GetServerRequest;
import net.creeperhost.minetogether.session.MineTogetherSession;
import net.creeperhost.minetogetherservers.util.MTSessionProvider;
import net.creeperhost.polylib.client.modulargui.ModularGuiInjector;
import net.creeperhost.polylib.client.screen.ButtonHelper;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Initialize on a client.
 * <p>
 * Created by covers1624 on 20/6/22.
 */
public class MineTogetherServersClient {

    private static final Logger LOGGER = LogManager.getLogger();

    private static boolean first = true;

    public static void init() {
        LOGGER.info("Initializing MineTogether Server List Client!");

        MineTogetherSession.getDefault().setProvider(new MTSessionProvider());
        MineTogetherSession.getDefault().onTokenRefreshed(token -> {
            MineTogetherServers.AUTH.setHeader("Authorization", "Bearer " + token);
        });
        // Trigger session validation and set auth header.
        MineTogetherSession.getDefault().getTokenAsync();

        MineTogetherServerList.init();


        ClientGuiEvent.INIT_POST.register(MineTogetherServersClient::onScreenOpen);
//        Integration.loadOptionalIntegration("ftbpc", () -> FTBPackCompanionCompat::init);
    }

    private static void onScreenOpen(Screen screen, ScreenAccess screenAccess) {
        if (screen instanceof TitleScreen && first) {
            first = false;
            String serverProp = System.getProperty("mt.server");
            if (serverProp == null) return;

            Server server;
            try {
                ApiClientResponse<GetServerRequest.Response> resp = MineTogetherServers.API.execute(new GetServerRequest(serverProp));
                if (resp.apiResponse().getStatus().equals("error")) {
                    LOGGER.error("Failed to load server with id: {}. Message: {}", serverProp, resp.apiResponse().getMessageOrNull());
                    return;
                }
                server = resp.apiResponse().server;
                if (server == null) {
                    LOGGER.error("Returned empty server?");
                    return;
                }
            } catch (IOException ex) {
                LOGGER.error("Failed to query server.", ex);
                return;
            }

            ServerData serverData = new ServerData(server.ip, String.valueOf(server.port), ServerData.Type.OTHER);
            ConnectScreen.startConnecting(new JoinMultiplayerScreen(screen), Minecraft.getInstance(), ServerAddress.parseString(serverData.ip), serverData, false, null);
        }
    }
}
