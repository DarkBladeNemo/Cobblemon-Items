package com.darkbladenemo.cobblemonextraitems.common.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.ShinyChanceCalculationEvent;
import com.darkbladenemo.cobblemonextraitems.common.util.CharmMultiplierUtils;

public class CharmEvents {

    public static void register() {
        CobblemonEvents.SHINY_CHANCE_CALCULATION.subscribe(Priority.NORMAL, event -> {
            handleShinyChance(event);
            return kotlin.Unit.INSTANCE;
        });
    }

    private static void handleShinyChance(ShinyChanceCalculationEvent event) {
        event.addModificationFunction((currentRate, player, pokemon) -> {
            if (player == null) {
                return currentRate;
            }

            float multiplier = CharmMultiplierUtils.getShinyMultiplier(player);

            // Only modify if charm is equipped (multiplier > 1)
            if (multiplier > 1.0f) {
                return currentRate / multiplier;
            }

            return currentRate;
        });
    }
}