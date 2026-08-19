package net.creeperhost.minetogetherservers;

import net.creeperhost.minetogetherservers.platform.IMineTogetherServersPlatform;
import net.creeperhost.polylib.platform.Services;
import net.minecraft.SharedConstants;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Locale;

public final class MineTogetherServersPlatform {

    private static final IMineTogetherServersPlatform PLATFORM = Services.load(IMineTogetherServersPlatform.class);

    private MineTogetherServersPlatform() {
    }

    @Nullable
    public static Path getModJar() {
        return PLATFORM.getModJar();
    }

    public static String getVersion() {
        return PLATFORM.getVersion();
    }

    public static Path getGameFolder() {
        Path configFolder = Services.PLATFORM.getConfigFolder().toAbsolutePath().normalize();
        Path gameFolder = configFolder.getParent();
        return gameFolder != null ? gameFolder : configFolder;
    }

    public static String getMinecraftVersion() {
        return SharedConstants.getCurrentVersion().name();
    }

    public static String getPlatformName() {
        return Services.PLATFORM.getPlatformName().toLowerCase(Locale.ROOT);
    }
}
