package com.darkbladenemo.cobblemonextraitems.common.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.ShinyChanceCalculationEvent;
import com.darkbladenemo.cobblemonextraitems.common.component.ShinyCharmData;
import com.darkbladenemo.cobblemonextraitems.common.config.Config;
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents;
import com.darkbladenemo.cobblemonextraitems.init.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public class CharmEvents {

    public static void register() {
        CobblemonEvents.SHINY_CHANCE_CALCULATION.subscribe(Priority.NORMAL, event -> {
            handleShinyChance(event);
            return kotlin.Unit.INSTANCE;
        });
    }

    private static void handleShinyChance(ShinyChanceCalculationEvent event) {
        event.addModificationFunction((currentRate, player, pokemon) -> {
            float rate = currentRate;

            if (player != null) {
                float multiplier = getShinyMultiplier(player);
                if (multiplier > 1.0f) {
                    rate /= multiplier;
                }
            }

            return rate;
        });
    }

    /**
     * Gets the shiny chance multiplier from equipped Shiny Charms.
     * Returns the first charm's multiplier found, or 1.0f if none equipped.
     */
    private static float getShinyMultiplier(ServerPlayer player) {
        float[] multiplier = {1.0f};

        CuriosApi.getCuriosInventory(player).ifPresent(inventory -> {
            inventory.findCurios("shiny_charm_slot").forEach(slotResult -> {
                ItemStack stack = slotResult.stack();
                if (!stack.isEmpty() && stack.is(ModItems.SHINY_CHARM.get())) {
                    ShinyCharmData data = stack.get(ModDataComponents.SHINY_CHARM_DATA.get());
                    if (data != null) {
                        multiplier[0] = data.multiplier();
                    } else {
                        // Fallback to config default if data is missing
                        multiplier[0] = Config.SHINY_CHARM_MULTIPLIER.get().floatValue();
                    }
                }
            });
        });

        return multiplier[0];
    }
}