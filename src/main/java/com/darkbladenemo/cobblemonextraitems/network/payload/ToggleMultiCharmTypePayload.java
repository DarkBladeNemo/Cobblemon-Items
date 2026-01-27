package com.darkbladenemo.cobblemonextraitems.network.payload;

import com.darkbladenemo.cobblemonextraitems.CobblemonExtraItemsMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record ToggleMultiCharmTypePayload(
        String typeName,
        int curioSlotIndex,
        boolean fromCurio
) implements CustomPacketPayload {
    public static final Type<ToggleMultiCharmTypePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CobblemonExtraItemsMod.MOD_ID, "toggle_multi_charm_type"));

    public static final StreamCodec<ByteBuf, ToggleMultiCharmTypePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    ToggleMultiCharmTypePayload::typeName,
                    ByteBufCodecs.INT,
                    ToggleMultiCharmTypePayload::curioSlotIndex,
                    ByteBufCodecs.BOOL,
                    ToggleMultiCharmTypePayload::fromCurio,
                    ToggleMultiCharmTypePayload::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}