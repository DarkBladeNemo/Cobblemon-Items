package com.darkbladenemo.cobblemonextraitems.network.payload;

import com.darkbladenemo.cobblemonextraitems.CobblemonExtraItemsMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record OpenMultiCharmScreenPayload(int slotIndex, boolean fromCurio) implements CustomPacketPayload {
    public static final Type<OpenMultiCharmScreenPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CobblemonExtraItemsMod.MOD_ID, "open_multi_charm_screen"));

    public static final StreamCodec<ByteBuf, OpenMultiCharmScreenPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    OpenMultiCharmScreenPayload::slotIndex,
                    ByteBufCodecs.BOOL,
                    OpenMultiCharmScreenPayload::fromCurio,
                    OpenMultiCharmScreenPayload::new
            );

    // Constructor for hand-held items (default behavior)
    public OpenMultiCharmScreenPayload() {
        this(-1, false);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}