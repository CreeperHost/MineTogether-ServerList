package net.creeperhost.minetogetherservers.compat;

import net.creeperhost.polylib.platform.Services;

import java.util.function.Supplier;

/**
 * Created by brandon3055 on 14/07/2024
 */
public class Integration {

    public static void runOptional(String modid, Supplier<Runnable> runnable) {
        if (Services.PLATFORM.isModLoaded(modid)) {
            runnable.get().run();
        }
    }

}
