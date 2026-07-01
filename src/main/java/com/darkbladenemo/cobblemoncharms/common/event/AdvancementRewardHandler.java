package com.darkbladenemo.cobblemoncharms.common.event;

import com.darkbladenemo.cobblemoncharms.advancement.ModAdvancement;
import com.darkbladenemo.cobblemoncharms.common.config.Config;
import com.darkbladenemo.cobblemoncharms.init.ModItems;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * Awards charm items at the moment their advancement is earned.
 * Called from AdvancementUtils.grantAdvancement() for programmatically granted advancements,
 * and from HandleAdvancement (every 20 ticks) to catch vanilla-triggered ones like
 * cobblemon:catch_pokemon and cobblemon:level_up.
 *
 * No login check — the tick loop handles detection promptly enough,
 * and a login check would give duplicates to players who stored their charm elsewhere.
 */
public class AdvancementRewardHandler {

    public static void register() {

    }

    public static void checkRewards(ServerPlayer player) {
        tryGiveExpCharm(player);
        tryGiveCatchCharm(player);
    }

    private static void tryGiveExpCharm(ServerPlayer player) {
        if (!Config.ENABLE_EXP_CHARM.get()) return;
        if (!Config.GRANT_CHARM_ON_ADVANCEMENT.get()) return;

        AdvancementHolder earned = ModAdvancement.EXP_CHARM.getAdvancement(player.serverLevel());
        AdvancementHolder rewarded = ModAdvancement.EXP_CHARM_REWARDED.getAdvancement(player.serverLevel());
        if (earned == null || rewarded == null) return;

        if (!player.getAdvancements().getOrStartProgress(earned).isDone()) return;
        if (player.getAdvancements().getOrStartProgress(rewarded).isDone()) return;

        // Mark rewarded before giving item to prevent double-giving
        for (String criterion : player.getAdvancements()
                .getOrStartProgress(rewarded).getRemainingCriteria()) {
            player.getAdvancements().award(rewarded, criterion);
        }

        ItemStack stack = new ItemStack(ModItems.EXP_CHARM);
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
        player.sendSystemMessage(
                Component.translatable("message.cobblemoncharms.exp_charm_awarded"));
    }

    private static void tryGiveCatchCharm(ServerPlayer player) {
        if (!Config.ENABLE_CATCH_CHARM.get()) return;
        if (!Config.GRANT_CHARM_ON_ADVANCEMENT.get()) return;

        AdvancementHolder earned = ModAdvancement.CATCH_CHARM.getAdvancement(player.serverLevel());
        AdvancementHolder rewarded = ModAdvancement.CATCH_CHARM_REWARDED.getAdvancement(player.serverLevel());
        if (earned == null || rewarded == null) return;

        if (!player.getAdvancements().getOrStartProgress(earned).isDone()) return;
        if (player.getAdvancements().getOrStartProgress(rewarded).isDone()) return;

        for (String criterion : player.getAdvancements()
                .getOrStartProgress(rewarded).getRemainingCriteria()) {
            player.getAdvancements().award(rewarded, criterion);
        }

        ItemStack stack = new ItemStack(ModItems.CATCH_CHARM);
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
        player.sendSystemMessage(
                Component.translatable("message.cobblemoncharms.catch_charm_awarded"));
    }
}