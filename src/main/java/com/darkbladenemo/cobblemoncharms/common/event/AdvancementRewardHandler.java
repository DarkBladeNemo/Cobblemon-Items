package com.darkbladenemo.cobblemoncharms.common.event;

import com.darkbladenemo.cobblemoncharms.advancement.ModAdvancement;
import com.darkbladenemo.cobblemoncharms.common.config.Config;
import com.darkbladenemo.cobblemoncharms.init.ModItems;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Awards charm items at the moment their advancement is earned.
 * Called from AdvancementUtils.grantAdvancement() for programmatically granted advancements,
 * and from HandleAdvancement (every 20 ticks) to catch vanilla-triggered ones like
 * cobblemon:catch_pokemon and cobblemon:level_up.
 * <p>
 * No login check — the tick loop handles detection promptly enough,
 * and a login check would give duplicates to players who stored their charm elsewhere.
 */
public class AdvancementRewardHandler {

    public static void register() {
    }

    public static void checkRewards(ServerPlayer player) {
        checkCharmReward(player,
                ModAdvancement.EXP_CHARM, ModAdvancement.EXP_CHARM_REWARDED,
                ModItems.EXP_CHARM, Config.ENABLE_EXP_CHARM, Config.GRANT_EXP_CHARM_ON_ADVANCEMENT,
                "message.cobblemoncharms.exp_charm_awarded");

        checkCharmReward(player,
                ModAdvancement.CATCH_CHARM, ModAdvancement.CATCH_CHARM_REWARDED,
                ModItems.CATCH_CHARM, Config.ENABLE_CATCH_CHARM, Config.GRANT_CATCH_CHARM_ON_ADVANCEMENT,
                "message.cobblemoncharms.catch_charm_awarded");
    }

    private static void checkCharmReward(
            ServerPlayer player,
            ModAdvancement earnedAdvancement,
            ModAdvancement rewardedAdvancement,
            Item charmItem,
            Config.BooleanValue enabledToggle,
            Config.BooleanValue grantToggle,
            String messageKey
    ) {
        if (!enabledToggle.get()) return;
        if (!Config.GRANT_CHARM_ON_ADVANCEMENT.get()) return;
        if (!grantToggle.get()) return;

        AdvancementHolder earned = earnedAdvancement.getAdvancement(player.serverLevel());
        AdvancementHolder rewarded = rewardedAdvancement.getAdvancement(player.serverLevel());
        if (earned == null || rewarded == null) return;

        if (!player.getAdvancements().getOrStartProgress(earned).isDone()) return;
        if (player.getAdvancements().getOrStartProgress(rewarded).isDone()) return;

        // Mark rewarded before giving item to prevent double-giving
        for (String criterion : player.getAdvancements()
                .getOrStartProgress(rewarded).getRemainingCriteria()) {
            player.getAdvancements().award(rewarded, criterion);
        }

        ItemStack stack = new ItemStack(charmItem);
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
        player.sendSystemMessage(Component.translatable(messageKey));
    }
}