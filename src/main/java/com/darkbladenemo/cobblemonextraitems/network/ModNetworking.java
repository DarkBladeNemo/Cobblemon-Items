package com.darkbladenemo.cobblemonextraitems.network;

import com.darkbladenemo.cobblemonextraitems.CobblemonExtraItemsMod;
import com.darkbladenemo.cobblemonextraitems.component.MultiCharmData;
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents;
import com.darkbladenemo.cobblemonextraitems.init.ModItems;
import com.darkbladenemo.cobblemonextraitems.item.charm.CharmType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import top.theillusivec4.curios.api.CuriosApi;

@EventBusSubscriber(modid = CobblemonExtraItemsMod.MOD_ID)
public class ModNetworking {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                OpenMultiCharmScreenPayload.TYPE,
                OpenMultiCharmScreenPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    // Handle on client - open screen
                    net.minecraft.client.Minecraft.getInstance().setScreen(
                            new com.darkbladenemo.cobblemonextraitems.client.screen.MultiCharmScreen(
                                    net.minecraft.client.Minecraft.getInstance().player,
                                    payload.slotIndex(),
                                    payload.fromCurio()
                            )
                    );
                })
        );

        registrar.playToServer(
                ToggleMultiCharmTypePayload.TYPE,
                ToggleMultiCharmTypePayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    ServerPlayer player = (ServerPlayer) context.player();

                    // Try to get from curio first, then hands
                    ItemStack stack = getMultiCharmStack(player, payload.curioSlotIndex(), payload.fromCurio());

                    if (!stack.isEmpty()) {
                        CharmType type = CharmType.fromString(payload.typeName());
                        if (type != null) {
                            MultiCharmData data = stack.get(ModDataComponents.MULTI_CHARM_DATA.get());
                            if (data != null && data.hasType(type)) {
                                MultiCharmData newData = data.toggleType(type);
                                stack.set(ModDataComponents.MULTI_CHARM_DATA.get(), newData);

                                // Send update back to client to refresh GUI
                                PacketDistributor.sendToPlayer(player, new RefreshMultiCharmScreenPayload(newData));
                            }
                        }
                    }
                })
        );

        registrar.playToClient(
                RefreshMultiCharmScreenPayload.TYPE,
                RefreshMultiCharmScreenPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    // Refresh the screen if it's open
                    if (net.minecraft.client.Minecraft.getInstance().screen instanceof
                            com.darkbladenemo.cobblemonextraitems.client.screen.MultiCharmScreen screen) {
                        screen.refreshData(payload.data());
                    }
                })
        );

        registrar.playToServer(
                OpenMultiCharmFromCurioPayload.TYPE,
                OpenMultiCharmFromCurioPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    ServerPlayer player = (ServerPlayer) context.player();

                    CuriosApi.getCuriosInventory(player).ifPresent(inventory -> {
                        var slots = inventory.findCurios("type_charm_slot");
                        int slotIndex = payload.slotIndex();

                        if (slotIndex >= 0 && slotIndex < slots.size()) {
                            ItemStack stack = slots.get(slotIndex).stack();
                            if (stack.is(ModItems.MULTI_CHARM.get())) {
                                // Send packet to open the GUI for this specific charm from curio
                                PacketDistributor.sendToPlayer(player,
                                        new OpenMultiCharmScreenPayload(slotIndex, true)
                                );
                            }
                        }
                    });
                })
        );
    }

    private static ItemStack getMultiCharmStack(ServerPlayer player, int curioSlotIndex, boolean fromCurio) {
        // If opened from curio, get from curio slot
        if (fromCurio && curioSlotIndex >= 0) {
            ItemStack[] result = {ItemStack.EMPTY};
            CuriosApi.getCuriosInventory(player).ifPresent(inventory -> {
                var slots = inventory.findCurios("type_charm_slot");
                if (curioSlotIndex < slots.size()) {
                    result[0] = slots.get(curioSlotIndex).stack();
                }
            });
            return result[0];
        }

        // Otherwise check hands (for shift-right-click)
        if (player.getMainHandItem().is(ModItems.MULTI_CHARM.get())) {
            return player.getMainHandItem();
        } else if (player.getOffhandItem().is(ModItems.MULTI_CHARM.get())) {
            return player.getOffhandItem();
        }
        return ItemStack.EMPTY;
    }
}