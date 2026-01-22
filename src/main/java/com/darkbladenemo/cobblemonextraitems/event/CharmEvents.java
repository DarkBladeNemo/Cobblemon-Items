package com.darkbladenemo.cobblemonextraitems.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.ShinyChanceCalculationEvent;
import com.darkbladenemo.cobblemonextraitems.config.Config;
import com.darkbladenemo.cobblemonextraitems.init.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

public class CharmEvents {

    public static void register() {
        CobblemonEvents.SHINY_CHANCE_CALCULATION.subscribe(Priority.NORMAL, event -> {
            handleShinyChance(event);
            return kotlin.Unit.INSTANCE;
        });
    }

    private static void handleShinyChance(ShinyChanceCalculationEvent event) {
        event.addModificationFunction((currentRate, player, pokemon) -> {
            float rate = currentRate; // This is the denominator (8192)

            if (player != null && hasShinyCharm(player)) {
                float multiplier = Config.SHINY_CHARM_MULTIPLIER.get().floatValue();
                rate /= multiplier; // 8192 / 3 = 2730 (for 3x boost)
            }

            return rate;
        });
    }

    private static boolean hasShinyCharm(ServerPlayer player) {
        Optional<Boolean> result = CuriosApi.getCuriosInventory(player).map(inventory ->
                inventory.findCurios("shiny_charm_slot").stream().anyMatch(slotResult -> {
                    ItemStack stack = slotResult.stack();
                    return stack.is(ModItems.SHINY_CHARM.get());
                })
        );
        return result.orElse(false);
    }
}