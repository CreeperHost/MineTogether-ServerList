package net.creeperhost.minetogetherservers;

import net.creeperhost.polylib.platform.Services;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * Created by covers1624 on 26/8/22.
 */
public interface MineTogetherServersPlatform {

    MineTogetherServersPlatform INSTANCE = Services.load(MineTogetherServersPlatform.class);

    @Nullable
    Path getModJar();

    String getVersion();
}
