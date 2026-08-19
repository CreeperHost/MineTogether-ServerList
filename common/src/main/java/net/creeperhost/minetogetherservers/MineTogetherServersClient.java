package net.creeperhost.minetogetherservers;

import net.creeperhost.minetogether.lib.web.ApiClientResponse;
import net.creeperhost.minetogether.session.MineTogetherSession;
import net.creeperhost.minetogetherservers.serverlist.MineTogetherServerList;
import net.creeperhost.minetogetherservers.serverlist.data.Server;
import net.creeperhost.minetogetherservers.serverlist.web.GetServerRequest;
import net.creeperhost.minetogetherservers.util.MTSessionProvider;
import net.creeperhost.polylib.event.events.client.PolyClientLifecycleEvents;
import net.creeperhost.polylib.event.events.client.PolyScreenEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

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

        PolyScreenEvents.SCREEN_OPENED.register(MineTogetherServersClient::onScreenOpen);

        //Cant do this in init anymore because init now occurs before Minecraft.instance is initialised.
        PolyClientLifecycleEvents.CLIENT_STARTED.register(instance -> {
            MineTogetherServerList.init();
            MineTogetherSession.getDefault().setProvider(new MTSessionProvider());
            MineTogetherSession.getDefault().onTokenRefreshed(token -> {
                MineTogetherServers.AUTH.setHeader("Authorization", "Bearer " + token);
            });
            // Trigger session validation and set auth header.
            MineTogetherSession.getDefault().getTokenAsync();
        });
    }

    private static void onScreenOpen(Minecraft minecraft, Screen screen, int width, int height) {
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
            ConnectScreen.startConnecting(new JoinMultiplayerScreen(screen), minecraft, ServerAddress.parseString(serverData.ip), serverData, false, null);
        }
    }
}
