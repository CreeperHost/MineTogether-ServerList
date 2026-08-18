package net.creeperhost.minetogetherservers.serverlist;

import com.google.common.hash.Hashing;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.hooks.client.screen.ScreenAccess;
import dev.architectury.hooks.client.screen.ScreenHooks;
import net.creeperhost.minetogether.lib.web.ApiClientResponse;
import net.creeperhost.minetogetherservers.MineTogetherServers;
import net.creeperhost.minetogetherservers.serverlist.data.ListType;
import net.creeperhost.minetogetherservers.serverlist.data.Server;
import net.creeperhost.minetogetherservers.serverlist.gui.ServerListGui;
import net.creeperhost.minetogetherservers.serverlist.web.GetServerListRequest;
import net.creeperhost.minetogetherservers.util.ModPackInfo;
import net.creeperhost.polylib.client.modulargui.ModularGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by covers1624 on 25/10/22.
 */
public class MineTogetherServerList {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final List<Server> servers = new LinkedList<>();
    @Nullable
    private static ListType lastRequest;
    private static long lastRequestTime = 0;
    private static boolean incorrectlyConfigured = true;
    private static String uuidHash;

    public static void init() {
        UUID uuid = Minecraft.getInstance().getUser().getProfileId();
        uuidHash = Hashing.sha256().hashString(uuid.toString(), UTF_8).toString().toUpperCase(Locale.ROOT);

        ModPackInfo.waitForInfo(versionInfo -> {
            incorrectlyConfigured = versionInfo.curseID.isEmpty() && versionInfo.base64FTBID.isEmpty();
            if (incorrectlyConfigured) {
                Server server = new Server();
                server.name = "No project ID! Please fix the MineTogether config or ensure a version.json exists.";
                synchronized (servers) {
                    servers.add(server);
                }
            }
        });

        ClientGuiEvent.INIT_POST.register(MineTogetherServerList::onScreenOpen);
    }

    public static List<Server> updateServers(ListType type) {
        synchronized (servers) {
            if (incorrectlyConfigured || lastRequest == type && lastRequestTime + 30000 < System.currentTimeMillis())
                return Collections.unmodifiableList(servers);

            try {
                ApiClientResponse<GetServerListRequest.Response> resp = MineTogetherServers.API.execute(new GetServerListRequest(type, uuidHash));
                servers.clear();
                servers.addAll(resp.apiResponse().servers);
            } catch (Throwable ex) {
                LOGGER.error("Failed to update server listings.", ex);
            }
            return Collections.unmodifiableList(servers);
        }
    }

    private static void onScreenOpen(Screen screen, ScreenAccess screenAccess) {
        if (!(screen instanceof JoinMultiplayerScreen mpScreen)) return;

        Button serverListButton = Button.builder(Component.translatable("minetogether:screen.multiplayer.serverlist"), e -> Minecraft.getInstance().gui.setScreen(new ModularGuiScreen(new ServerListGui(), mpScreen)))
                .bounds(screen.width - 105, 5, 100, 20)
                .build();
//        serverListButton.active = !MineTogetherChat.isNewUser();
        ScreenHooks.addRenderableWidget(mpScreen, serverListButton);
    }
}
