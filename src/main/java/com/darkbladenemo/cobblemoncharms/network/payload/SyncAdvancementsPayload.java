package com.darkbladenemo.cobblemoncharms.network.payload;

import com.darkbladenemo.cobblemoncharms.CobblemonCharmsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Sent server → client on login and after each advancement grant.
 * Carries the full list of mod advancement IDs the player has earned so the
 * client can update its local cache for tooltip display.
 */
public record SyncAdvancementsPayload(List<ResourceLocation> earnedIds) implements CustomPacketPayload {

    public static final Type<SyncAdvancementsPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CobblemonCharmsMod.MOD_ID, "sync_advancements"));

    public static final StreamCodec<FriendlyByteBuf, SyncAdvancementsPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> {
                        buf.writeVarInt(payload.earnedIds().size());
                        for (ResourceLocation id : payload.earnedIds()) {
                            buf.writeResourceLocation(id);
                        }
                    },
                    buf -> {
                        int size = buf.readVarInt();
                        List<ResourceLocation> ids = new ArrayList<>(size);
                        for (int i = 0; i < size; i++) {
                            ids.add(buf.readResourceLocation());
                        }
                        return new SyncAdvancementsPayload(ids);
                    }
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}