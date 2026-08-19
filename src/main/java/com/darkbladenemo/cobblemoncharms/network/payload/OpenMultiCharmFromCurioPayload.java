package com.darkbladenemo.cobblemoncharms.network.payload;

import com.darkbladenemo.cobblemoncharms.CobblemonCharmsMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record OpenMultiCharmFromCurioPayload(int slotIndex) implements CustomPacketPayload {
    public static final Type<OpenMultiCharmFromCurioPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CobblemonCharmsMod.MOD_ID, "open_multi_charm_from_curio"));

    public static final StreamCodec<ByteBuf, OpenMultiCharmFromCurioPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    OpenMultiCharmFromCurioPayload::slotIndex,
                    OpenMultiCharmFromCurioPayload::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}