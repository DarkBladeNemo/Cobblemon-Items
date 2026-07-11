package com.darkbladenemo.cobblemoncharms.common.event;

import com.darkbladenemo.cobblemoncharms.advancement.ModAdvancement;
import com.darkbladenemo.cobblemoncharms.network.ModNetworking;
import com.darkbladenemo.cobblemoncharms.network.payload.SyncAdvancementsPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps the client's advancement cache and config values in sync.
 * Sends a full snapshot on login and periodically to catch any changes.
 */
public class AdvancementSyncEvents {

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            sendSnapshot(handler.player);
            // Sync config on join so client tooltips reflect server config
            ServerPlayNetworking.send(handler.player, ModNetworking.buildConfigSnapshot());
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getTickCount() % 100 != 0) return;
            server.getPlayerList().getPlayers().forEach(AdvancementSyncEvents::sendSnapshot);
        });
    }

    public static void sendSnapshot(ServerPlayer player) {
        ServerPlayNetworking.send(player, buildSnapshot(player));
    }

    private static SyncAdvancementsPayload buildSnapshot(ServerPlayer player) {
        List<ResourceLocation> earned = new ArrayList<>();

        for (ModAdvancement modAdv : ModAdvancement.values()) {
            AdvancementHolder holder = modAdv.getAdvancement(player.serverLevel());
            if (holder != null &&
                    player.getAdvancements().getOrStartProgress(holder).isDone()) {
                earned.add(holder.id());
            }
        }

        return new SyncAdvancementsPayload(earned);
    }
}