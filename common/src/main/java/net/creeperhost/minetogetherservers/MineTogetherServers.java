package net.creeperhost.minetogetherservers;

import net.covers1624.quack.net.httpapi.HttpEngine;
import net.covers1624.quack.net.httpapi.java11.Java11HttpEngine;
import net.creeperhost.minetogether.lib.MineTogetherLib;
import net.creeperhost.minetogether.lib.web.ApiClient;
import net.creeperhost.minetogether.lib.web.DynamicWebAuth;
import net.creeperhost.minetogetherservers.config.Config;
import net.creeperhost.minetogetherservers.util.Log4jUtils;
import net.creeperhost.minetogetherservers.util.ModPackInfo;
import net.creeperhost.minetogetherservers.util.SignatureVerifier;
import net.creeperhost.polylib.platform.Services;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main common mod entrypoint.
 * <p>
 * Created by covers1624 on 20/6/22.
 */
public class MineTogetherServers {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "minetogetherservers";

    public static final String FINGERPRINT = SignatureVerifier.generateSignature();
    public static final DynamicWebAuth AUTH = new DynamicWebAuth();
    public static final HttpEngine WEB_ENGINE = Java11HttpEngine.create();
    public static final ApiClient API = ApiClient.builder()
            .httpEngine(WEB_ENGINE)
            .addUserAgentSegment("MineTogether-lib/" + MineTogetherLib.VERSION)
            .addUserAgentSegment("MineTogether-Servers-mod/" + MineTogetherServersPlatform.getVersion())
            .addUserAgentSegment("Minecraft/" + MineTogetherServersPlatform.getMinecraftVersion())
            .addUserAgentSegment("Modloader/" + MineTogetherServersPlatform.getPlatformName())
            .webAuth(AUTH)
            .build();

    public static void init() {
        Log4jUtils.attachMTLogs(MineTogetherServersPlatform.getGameFolder().resolve("logs"));
        LOGGER.info("Initializing MineTogether Server List!");
        AUTH.setHeader("Fingerprint", FINGERPRINT);

        if (Config.instance().debugMode) {
            LOGGER.warn("Debug mode enabled. Prepare for _VERY_ verbose logging!");
        }

        ModPackInfo.init();
        ModPackInfo.waitForInfo(info -> AUTH.setHeader("Identifier", info.realName));
        if (Services.PLATFORM.isClient()) {
            MineTogetherServersClient.init();
        } else {
            MineTogetherServersServer.init();
        }
    }
}
