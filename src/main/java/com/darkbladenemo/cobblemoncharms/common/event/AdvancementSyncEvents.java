package com.darkbladenemo.cobblemoncharms.common.event;

import com.darkbladenemo.cobblemoncharms.cobblemoncharmsMod;
import com.darkbladenemo.cobblemoncharms.advancement.ModAdvancement;
import com.darkbladenemo.cobblemoncharms.network.ModNetworking;
import com.darkbladenemo.cobblemoncharms.network.payload.SyncAdvancementsPayload;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps the client's advancement cache and config values in sync.
 * Sends a full snapshot on login and after each mod advancement is earned.
 */
@EventBusSubscriber(modid = cobblemoncharmsMod.MOD_ID)
public class AdvancementSyncEvents {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, buildSnapshot(player));
            // Sync config on login so client tooltips reflect server config
            PacketDistributor.sendToPlayer(player, ModNetworking.buildConfigSnapshot());
        }
    }

    @SubscribeEvent
    public static void onAdvancementEarned(AdvancementEvent.AdvancementEarnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ResourceLocation id = event.getAdvancement().id();
            if (id.getNamespace().equals(cobblemoncharmsMod.MOD_ID)) {
                PacketDistributor.sendToPlayer(player, buildSnapshot(player));
            }
        }
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