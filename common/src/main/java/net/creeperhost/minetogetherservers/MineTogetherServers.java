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
import net.minecraft.SharedConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Locale;

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
            .addUserAgentSegment("MineTogether-Servers-mod/" + MineTogetherServersPlatform.INSTANCE.getVersion())
            .addUserAgentSegment("Minecraft/" + minecraftVersion())
            .addUserAgentSegment("Modloader/" + Services.PLATFORM.getPlatformName().toLowerCase(Locale.ROOT))
            .webAuth(AUTH)
            .build();

    public static void init() {
        Log4jUtils.attachMTLogs(gameFolder().resolve("logs"));
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

    public static Path gameFolder() {
        Path configFolder = Services.PLATFORM.getConfigFolder().toAbsolutePath().normalize();
        Path parent = configFolder.getParent();
        return parent != null ? parent : Path.of("").toAbsolutePath().normalize();
    }

    public static String minecraftVersion() {
        return SharedConstants.getCurrentVersion().id();
    }
}
