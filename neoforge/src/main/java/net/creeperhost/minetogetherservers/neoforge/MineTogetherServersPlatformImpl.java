package net.creeperhost.minetogetherservers.neoforge;

import net.creeperhost.minetogetherservers.MineTogetherServers;
import net.creeperhost.minetogetherservers.MineTogetherServersPlatform;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModFileInfo;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * Created by covers1624 on 26/8/22.
 */
public class MineTogetherServersPlatformImpl implements MineTogetherServersPlatform {

    @Nullable
    public Path getModJar() {
        IModFileInfo fileInfo = ModList.get().getModFileById(MineTogetherServers.MOD_ID);
        if (fileInfo == null) {
            return null;
        }
        return fileInfo.getFile().getFilePath();
    }

    public String getVersion() {
        IModFileInfo fileInfo = ModList.get().getModFileById(MineTogetherServers.MOD_ID);
        if (fileInfo == null) {
            return "UNKNOWN";
        }

        return fileInfo.versionString();
    }
}
