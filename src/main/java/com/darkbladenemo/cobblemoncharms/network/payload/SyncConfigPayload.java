package com.darkbladenemo.cobblemoncharms.network.payload;

import com.darkbladenemo.cobblemoncharms.cobblemoncharmsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Sent server → client on login to sync config values needed for tooltip display.
 * Ensures tooltips reflect server config regardless of the client's local config file.
 */
public record SyncConfigPayload(
        boolean charmEffectRequiresAdvancement,
        boolean grantCharmOnAdvancement,
        float shinyCharmMultiplier,
        float expCharmMultiplier,
        float catchCharmMultiplier,
        float typeCharmMatchMultiplier,
        float typeCharmNonMatchMultiplier,
        double typeCharmRadius,
        double typeCharmThresholdPercentage
) implements CustomPacketPayload {

    public static final Type<SyncConfigPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(cobblemoncharmsMod.MOD_ID, "sync_config"));

    public static final StreamCodec<FriendlyByteBuf, SyncConfigPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> {
                        buf.writeBoolean(payload.charmEffectRequiresAdvancement());
                        buf.writeBoolean(payload.grantCharmOnAdvancement());
                        buf.writeFloat(payload.shinyCharmMultiplier());
                        buf.writeFloat(payload.expCharmMultiplier());
                        buf.writeFloat(payload.catchCharmMultiplier());
                        buf.writeFloat(payload.typeCharmMatchMultiplier());
                        buf.writeFloat(payload.typeCharmNonMatchMultiplier());
                        buf.writeDouble(payload.typeCharmRadius());
                        buf.writeDouble(payload.typeCharmThresholdPercentage());
                    },
                    buf -> new SyncConfigPayload(
                            buf.readBoolean(),
                            buf.readBoolean(),
                            buf.readFloat(),
                            buf.readFloat(),
                            buf.readFloat(),
                            buf.readFloat(),
                            buf.readFloat(),
                            buf.readDouble(),
                            buf.readDouble()
                    )
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}