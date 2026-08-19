package com.darkbladenemo.cobblemoncharms.network.payload;

import com.darkbladenemo.cobblemoncharms.CobblemonCharmsMod;
import com.darkbladenemo.cobblemoncharms.common.component.MultiCharmData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record RefreshMultiCharmScreenPayload(MultiCharmData data) implements CustomPacketPayload {
    public static final Type<RefreshMultiCharmScreenPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CobblemonCharmsMod.MOD_ID, "refresh_multi_charm_screen"));

    public static final StreamCodec<ByteBuf, RefreshMultiCharmScreenPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.fromCodec(MultiCharmData.CODEC),
                    RefreshMultiCharmScreenPayload::data,
                    RefreshMultiCharmScreenPayload::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}