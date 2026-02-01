package com.darkbladenemo.cobblemonextraitems.common.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.ExperienceGainedEvent;
import com.darkbladenemo.cobblemonextraitems.common.util.CharmMultiplierUtils;
import kotlin.Unit;
import net.minecraft.server.level.ServerPlayer;

public class ExpCharmEvents {

    public static void register() {
        CobblemonEvents.EXPERIENCE_GAINED_EVENT_PRE.subscribe(Priority.NORMAL, event -> {
            handleExpEvent(event);
            return Unit.INSTANCE;
        });
    }

    private static void handleExpEvent(ExperienceGainedEvent.Pre event) {
        ServerPlayer player = event.getPokemon().getOwnerPlayer();
        if (player == null) {
            return;
        }

        float multiplier = CharmMultiplierUtils.getExpMultiplier(player);

        // Only modify if charm is equipped (multiplier > 1)
        if (multiplier <= 1.0f) {
            return;
        }

        int originalExp = event.getExperience();
        int newExp = Math.round(originalExp * multiplier);

        // Ensure at least 1 EXP gain if original was positive
        if (newExp <= 0 && originalExp > 0) {
            newExp = 1;
        }

        event.setExperience(newExp);
    }
}