package net.creeperhost.minetogetherservers.platform;

import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public interface IMineTogetherServersPlatform {

    @Nullable
    Path getModJar();

    String getVersion();
}
