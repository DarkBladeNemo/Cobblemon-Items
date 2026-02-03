package com.darkbladenemo.cobblemonextraitems.client.keybind;

import com.darkbladenemo.cobblemonextraitems.CobblemonExtraItemsMod;
import com.darkbladenemo.cobblemonextraitems.init.ModItems;
import com.darkbladenemo.cobblemonextraitems.network.payload.OpenMultiCharmFromCurioPayload;
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

@EventBusSubscriber(modid = CobblemonExtraItemsMod.MOD_ID, value = Dist.CLIENT)
public class KeyInputHandler {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player == null) return;

        if (ModKeyBindings.OPEN_MULTI_CHARM_GUI.consumeClick()) {
            // Find all Multi-Charms in type charm curio slots ONLY
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
                // If only one Multi-Charm, open it directly
                // If multiple, open selection GUI
                if (multiCharmSlots.size() == 1) {
                    PacketDistributor.sendToServer(new OpenMultiCharmFromCurioPayload(multiCharmSlots.getFirst()));
                } else {
                    // Open selection screen
                    minecraft.setScreen(new com.darkbladenemo.cobblemonextraitems.client.gui.MultiCharmSelectionScreen(
                            player, multiCharmSlots
                    ));
                }
            }
        }
    }
}