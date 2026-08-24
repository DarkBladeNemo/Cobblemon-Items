package com.darkbladenemo.cobblemoncharms.tick;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class TickManager {

    /** Registers the server tick listener. Call during mod construction. */
    public static void register() {
        NeoForge.EVENT_BUS.addListener(TickManager::onServerTick);
    }

    private static void onServerTick(ServerTickEvent.Post event) {
        // Check once per second (every 20 ticks) to keep overhead low
        if (event.getServer().getTickCount() % 20 == 0) {
            HandleAdvancement.checkAdvancements(event.getServer());
        }
    }
}