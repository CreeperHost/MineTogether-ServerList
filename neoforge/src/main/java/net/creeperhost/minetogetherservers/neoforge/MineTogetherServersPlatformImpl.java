package net.creeperhost.minetogetherservers.neoforge;

import net.creeperhost.minetogetherservers.MineTogetherServers;
import net.minecraft.network.Connection;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModFileInfo;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * Created by covers1624 on 26/8/22.
 */
public class MineTogetherServersPlatformImpl {

    @Nullable
    public static Path getModJar() {
        IModFileInfo fileInfo = ModList.get().getModFileById(MineTogetherServers.MOD_ID);
        if (fileInfo == null) {
            return null;
        }
        return fileInfo.getFile().getFilePath();
    }

    public static String getVersion() {
        IModFileInfo fileInfo = ModList.get().getModFileById(MineTogetherServers.MOD_ID);
        if (fileInfo == null) {
            return "UNKNOWN";
        }

        return fileInfo.versionString();
    }

    public static void prepareClientConnection(Connection connection) {
        // Not required on NeoForge.
    }
}
