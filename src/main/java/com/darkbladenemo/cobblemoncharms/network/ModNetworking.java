package com.darkbladenemo.cobblemoncharms.network;

import com.darkbladenemo.cobblemoncharms.common.component.MultiCharmData;
import com.darkbladenemo.cobblemoncharms.common.config.Config;
import com.darkbladenemo.cobblemoncharms.common.item.charm.CharmType;
import com.darkbladenemo.cobblemoncharms.init.ModDataComponents;
import com.darkbladenemo.cobblemoncharms.init.ModItems;
import com.darkbladenemo.cobblemoncharms.network.payload.*;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ModNetworking {

    public static void registerServer() {
        // S2C
        PayloadTypeRegistry.playS2C().register(
                OpenMultiCharmScreenPayload.TYPE,
                OpenMultiCharmScreenPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(
                RefreshMultiCharmScreenPayload.TYPE,
                RefreshMultiCharmScreenPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(
                SyncAdvancementsPayload.TYPE,
                SyncAdvancementsPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(
                SyncConfigPayload.TYPE,
                SyncConfigPayload.STREAM_CODEC);

        // C2S
        PayloadTypeRegistry.playC2S().register(
                ToggleMultiCharmTypePayload.TYPE,
                ToggleMultiCharmTypePayload.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(
                OpenMultiCharmFromCurioPayload.TYPE,
                OpenMultiCharmFromCurioPayload.STREAM_CODEC);

        // C2S handlers
        ServerPlayNetworking.registerGlobalReceiver(
                ToggleMultiCharmTypePayload.TYPE,
                (payload, context) -> {
                    ServerPlayer player = context.player();
                    context.server().execute(() -> {
                        ItemStack stack = getMultiCharmStack(
                                player, payload.curioSlotIndex(), payload.fromCurio());
                        if (stack.isEmpty()) return;

                        CharmType type = CharmType.fromString(payload.typeName());
                        if (type == null) return;

                        MultiCharmData data = stack.get(ModDataComponents.MULTI_CHARM_DATA);
                        if (data == null || !data.hasType(type)) return;

                        MultiCharmData newData = data.toggleType(type);
                        stack.set(ModDataComponents.MULTI_CHARM_DATA, newData);
                        ServerPlayNetworking.send(player,
                                new RefreshMultiCharmScreenPayload(newData));
                    });
                });

        ServerPlayNetworking.registerGlobalReceiver(
                OpenMultiCharmFromCurioPayload.TYPE,
                (payload, context) -> {
                    ServerPlayer player = context.player();
                    context.server().execute(() -> {
                        TrinketsApi.getTrinketComponent(player).ifPresent(trinkets -> {
                            var slots = trinkets.getEquipped(item ->
                                    item.is(ModItems.MULTI_CHARM));
                            int slotIndex = payload.slotIndex();
                            if (slotIndex >= 0 && slotIndex < slots.size()) {
                                ItemStack stack = slots.get(slotIndex).getB();
                                if (stack.is(ModItems.MULTI_CHARM)) {
                                    ServerPlayNetworking.send(player,
                                            new OpenMultiCharmScreenPayload(slotIndex, true));
                                }
                            }
                        });
                    });
                });
    }

    public static SyncConfigPayload buildConfigSnapshot() {
        return new SyncConfigPayload(
                Config.CHARM_EFFECT_REQUIRES_ADVANCEMENT.get(),
                Config.GRANT_CHARM_ON_ADVANCEMENT.get(),
                Config.SHINY_CHARM_MULTIPLIER.floatValue(),
                Config.EXP_CHARM_MULTIPLIER.floatValue(),
                Config.CATCH_CHARM_MULTIPLIER.floatValue(),
                Config.TYPE_CHARM_MATCH_MULTIPLIER.floatValue(),
                Config.TYPE_CHARM_NON_MATCH_MULTIPLIER.floatValue(),
                Config.TYPE_CHARM_RADIUS.get(),
                Config.TYPE_CHARM_THRESHOLD_PERCENTAGE.get()
        );
    }

    static ItemStack getMultiCharmStack(ServerPlayer player, int slotIndex, boolean fromTrinket) {
        if (fromTrinket && slotIndex >= 0) {
            ItemStack[] result = {ItemStack.EMPTY};
            TrinketsApi.getTrinketComponent(player).ifPresent(trinkets -> {
                var slots = trinkets.getEquipped(item -> item.is(ModItems.MULTI_CHARM));
                if (slotIndex < slots.size()) {
                    result[0] = slots.get(slotIndex).getB();
                }
            });
            return result[0];
        }

        if (player.getMainHandItem().is(ModItems.MULTI_CHARM)) return player.getMainHandItem();
        if (player.getOffhandItem().is(ModItems.MULTI_CHARM))  return player.getOffhandItem();
        return ItemStack.EMPTY;
    }
}