package com.darkbladenemo.cobblemoncharms.client.keybind;

import com.darkbladenemo.cobblemoncharms.CobblemonCharmsMod;
import com.darkbladenemo.cobblemoncharms.init.ModItems;
import com.darkbladenemo.cobblemoncharms.network.payload.OpenMultiCharmFromCurioPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = CobblemonCharmsMod.MOD_ID, value = Dist.CLIENT)
public class KeyInputHandler {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player == null) return;

        if (ModKeyBindings.OPEN_MULTI_CHARM_GUI.consumeClick()) {
            // Collect all Multi-Charms in "type_charm_slot" curio slots
            List<Integer> multiCharmSlots = new ArrayList<>();

            CuriosApi.getCuriosInventory(player).ifPresent(inventory -> {
                var slots = inventory.findCurios("type_charm_slot");
                for (int i = 0; i < slots.size(); i++) {
                    ItemStack stack = slots.get(i).stack();
                    if (stack.is(ModItems.MULTI_CHARM.get())) {
                        multiCharmSlots.add(i);
                    }
                }
            });

            if (!multiCharmSlots.isEmpty()) {
                if (multiCharmSlots.size() == 1) {
                    // Open the single Multi-Charm directly
                    PacketDistributor.sendToServer(new OpenMultiCharmFromCurioPayload(multiCharmSlots.getFirst()));
                } else {
                    // Multiple Multi-Charms: show selection GUI
                    minecraft.setScreen(new com.darkbladenemo.cobblemoncharms.client.gui.MultiCharmSelectionScreen(
                            player, multiCharmSlots
                    ));
                }
            }
        }
    }
}