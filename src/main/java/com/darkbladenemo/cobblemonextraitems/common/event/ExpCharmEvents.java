package com.darkbladenemo.cobblemonextraitems.common.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.ExperienceGainedEvent;
import com.darkbladenemo.cobblemonextraitems.common.component.ExpCharmData;
import com.darkbladenemo.cobblemonextraitems.common.config.Config;
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents;
import com.darkbladenemo.cobblemonextraitems.init.ModItems;
import kotlin.Unit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

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

        float multiplier = getExpMultiplier(player);
        if (multiplier <= 1.0f) {
            return; // No charm or multiplier is 1.0
        }

        int originalExp = event.getExperience();
        int newExp = (int) (originalExp * multiplier);

        // Ensure at least 1 EXP gain
        if (newExp <= 0 && originalExp > 0) {
            newExp = 1;
        }

        event.setExperience(newExp);
    }

    private static float getExpMultiplier(ServerPlayer player) {
        final float[] multiplier = {1.0f};

        CuriosApi.getCuriosInventory(player).ifPresent(inventory -> {
            inventory.findCurios("exp_charm_slot").forEach(slotResult -> {
                ItemStack stack = slotResult.stack();
                if (!stack.isEmpty() && stack.is(ModItems.EXP_CHARM.get())) {
                    ExpCharmData data = stack.get(ModDataComponents.EXP_CHARM_DATA.get());
                    if (data != null) {
                        multiplier[0] = data.multiplier();
                    } else {
                        // Fallback to config default if data is missing
                        multiplier[0] = Config.EXP_CHARM_MULTIPLIER.get().floatValue();
                    }
                }
            });
        });

        return multiplier[0];
    }
}