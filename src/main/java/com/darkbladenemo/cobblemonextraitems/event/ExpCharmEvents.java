package com.darkbladenemo.cobblemonextraitems.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.ExperienceGainedEvent;
import com.darkbladenemo.cobblemonextraitems.config.Config;
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
        if (player == null || !hasExpCharm(player)) {
            return;
        }

        int originalExp = event.getExperience();
        float multiplier = Config.EXP_CHARM_MULTIPLIER.get().floatValue();
        int newExp = (int) (originalExp * multiplier);

        // Ensure at least 1 EXP gain
        if (newExp <= 0 && originalExp > 0) {
            newExp = 1;
        }

        event.setExperience(newExp);
    }

    private static boolean hasExpCharm(ServerPlayer player) {
        return CuriosApi.getCuriosInventory(player).map(inventory ->
                inventory.findCurios("exp_charm_slot").stream()
                        .anyMatch(slotResult -> {
                            ItemStack stack = slotResult.stack();
                            return !stack.isEmpty() && stack.is(ModItems.EXP_CHARM.get());
                        })
        ).orElse(false);
    }
}