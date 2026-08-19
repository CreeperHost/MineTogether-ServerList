package net.creeperhost.minetogetherservers.util;

import com.google.common.hash.Hashing;
import net.covers1624.quack.util.HashUtils;
import net.creeperhost.minetogetherservers.MineTogetherServersPlatform;
import net.creeperhost.polylib.platform.Services;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class SignatureVerifier {

    private static final Logger LOGGER = LogManager.getLogger();

    public static String generateSignature() {
        if (!Services.PLATFORM.isDevelopmentEnvironment() && System.getProperty("mt.develop.signature") == null) {
            Path modJar = MineTogetherServersPlatform.INSTANCE.getModJar();
            if (modJar != null && modJar.toString().endsWith(".jar")) {
                try {
                    return HashUtils.hash(Hashing.sha256(), modJar).toString();
                } catch (IOException ex) {
                    LOGGER.error("Failed to hash mod jar.", ex);
                }
            }
        }
        return System.getProperty("mt.develop.signature", "Development");
    }
}
