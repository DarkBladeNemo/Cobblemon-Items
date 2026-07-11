package com.darkbladenemo.cobblemoncharms.network;

import com.darkbladenemo.cobblemoncharms.client.network.ClientPacketHandlers;
import com.darkbladenemo.cobblemoncharms.client.util.ClientAdvancementCache;
import com.darkbladenemo.cobblemoncharms.common.config.Config;
import com.darkbladenemo.cobblemoncharms.network.payload.OpenMultiCharmScreenPayload;
import com.darkbladenemo.cobblemoncharms.network.payload.RefreshMultiCharmScreenPayload;
import com.darkbladenemo.cobblemoncharms.network.payload.SyncAdvancementsPayload;
import com.darkbladenemo.cobblemoncharms.network.payload.SyncConfigPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public class ModNetworkingClient {

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(
                OpenMultiCharmScreenPayload.TYPE,
                (payload, context) -> context.client().execute(() ->
                        ClientPacketHandlers.handleOpenMultiCharmScreen(
                                payload.slotIndex(), payload.fromCurio())
                ));

        ClientPlayNetworking.registerGlobalReceiver(
                RefreshMultiCharmScreenPayload.TYPE,
                (payload, context) -> context.client().execute(() ->
                        ClientPacketHandlers.handleRefreshMultiCharmScreen(payload.data())
                ));

        ClientPlayNetworking.registerGlobalReceiver(
                SyncAdvancementsPayload.TYPE,
                (payload, context) -> context.client().execute(() ->
                        ClientAdvancementCache.INSTANCE.update(payload.earnedIds())
                ));

        ClientPlayNetworking.registerGlobalReceiver(
                SyncConfigPayload.TYPE,
                (payload, context) -> context.client().execute(() ->
                        Config.syncFromServer(
                                payload.charmEffectRequiresAdvancement(),
                                payload.grantCharmOnAdvancement(),
                                payload.shinyCharmMultiplier(),
                                payload.expCharmMultiplier(),
                                payload.catchCharmMultiplier(),
                                payload.typeCharmMatchMultiplier(),
                                payload.typeCharmNonMatchMultiplier(),
                                payload.typeCharmRadius(),
                                payload.typeCharmThresholdPercentage()
                        )
                ));
    }
}