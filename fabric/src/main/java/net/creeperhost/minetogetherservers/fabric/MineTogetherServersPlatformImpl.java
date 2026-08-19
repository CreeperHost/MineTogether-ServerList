package net.creeperhost.minetogetherservers.fabric;

import net.creeperhost.minetogetherservers.MineTogetherServers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModOrigin;
import net.creeperhost.minetogetherservers.MineTogetherServersPlatform;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Created by covers1624 on 26/8/22.
 */
public class MineTogetherServersPlatformImpl implements MineTogetherServersPlatform {

    @Nullable
    public Path getModJar() {
        Optional<ModContainer> container = FabricLoader.getInstance()
                .getModContainer(MineTogetherServers.MOD_ID);
        if (container.isEmpty()) return null;

        ModOrigin origin = container.get().getOrigin();
        if (origin.getKind() != ModOrigin.Kind.PATH) {
            return null;
        }
        List<Path> paths = origin.getPaths();
        return !paths.isEmpty() ? paths.get(0) : null;
    }

    public String getVersion() {
        Optional<ModContainer> container = FabricLoader.getInstance()
                .getModContainer(MineTogetherServers.MOD_ID);
        if (container.isEmpty()) return "UNKNOWN";

        return container.get().getMetadata().getVersion().getFriendlyString();
    }
}
