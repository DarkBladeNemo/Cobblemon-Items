package com.darkbladenemo.cobblemoncharms.common.event;

import com.darkbladenemo.cobblemoncharms.advancement.ModAdvancement;
import com.darkbladenemo.cobblemoncharms.common.config.Config;
import com.darkbladenemo.cobblemoncharms.init.ModItems;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;

/**
 * Listens for advancements being earned and gives the player the corresponding item reward.
 */
public class AdvancementRewardHandler {

    public static void register() {
        NeoForge.EVENT_BUS.addListener(AdvancementRewardHandler::onAdvancementEarned);
    }

    private static void onAdvancementEarned(AdvancementEvent.AdvancementEarnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        AdvancementHolder earned = event.getAdvancement();

        checkCharmReward(player, earned, ModAdvancement.EXP_CHARM, ModItems.EXP_CHARM.get(),
                Config.ENABLE_EXP_CHARM, Config.GRANT_EXP_CHARM_ON_ADVANCEMENT,
                "message.cobblemoncharms.exp_charm_awarded");

        checkCharmReward(player, earned, ModAdvancement.CATCH_CHARM, ModItems.CATCH_CHARM.get(),
                Config.ENABLE_CATCH_CHARM, Config.GRANT_CATCH_CHARM_ON_ADVANCEMENT,
                "message.cobblemoncharms.catch_charm_awarded");
    }

    private static void checkCharmReward(
            ServerPlayer player,
            AdvancementHolder earned,
            ModAdvancement rewardAdvancement,
            Item charmItem,
            ModConfigSpec.BooleanValue enabledToggle,
            ModConfigSpec.BooleanValue grantToggle,
            String messageKey
    ) {
        if (!enabledToggle.get()) return;
        if (!Config.GRANT_CHARM_ON_ADVANCEMENT.get()) return;
        if (!grantToggle.get()) return;

        AdvancementHolder holder = rewardAdvancement.getAdvancement(player.serverLevel());
        if (holder == null || !earned.id().equals(holder.id())) return;

        giveItem(player, new ItemStack(charmItem), messageKey);
    }

    private static void giveItem(ServerPlayer player, ItemStack stack, String messageKey) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
        player.sendSystemMessage(Component.translatable(messageKey));
    }
}