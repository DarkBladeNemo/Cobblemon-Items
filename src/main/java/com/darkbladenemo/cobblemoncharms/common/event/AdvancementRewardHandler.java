package com.darkbladenemo.cobblemoncharms.common.event;

import com.darkbladenemo.cobblemoncharms.advancement.ModAdvancement;
import com.darkbladenemo.cobblemoncharms.common.config.Config;
import com.darkbladenemo.cobblemoncharms.init.ModItems;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
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

        checkExpCharm(player, earned);
        checkCatchCharm(player, earned);
    }

    private static void checkExpCharm(ServerPlayer player, AdvancementHolder earned) {
        if (!Config.ENABLE_EXP_CHARM.get()) return;
        if (!Config.GRANT_CHARM_ON_ADVANCEMENT.get()) return;

        AdvancementHolder expCharmAdvancement =
                ModAdvancement.EXP_CHARM.getAdvancement(player.serverLevel());

        if (expCharmAdvancement == null) return;
        if (!earned.id().equals(expCharmAdvancement.id())) return;

        giveItem(player, new ItemStack(ModItems.EXP_CHARM.get()),
                "message.cobblemoncharms.exp_charm_awarded");
    }

    private static void checkCatchCharm(ServerPlayer player, AdvancementHolder earned) {
        if (!Config.ENABLE_CATCH_CHARM.get()) return;
        if (!Config.GRANT_CHARM_ON_ADVANCEMENT.get()) return;

        AdvancementHolder catchCharmAdvancement =
                ModAdvancement.CATCH_CHARM.getAdvancement(player.serverLevel());

        if (catchCharmAdvancement == null) return;
        if (!earned.id().equals(catchCharmAdvancement.id())) return;

        giveItem(player, new ItemStack(ModItems.CATCH_CHARM.get()),
                "message.cobblemoncharms.catch_charm_awarded");
    }

    private static void giveItem(ServerPlayer player, ItemStack stack, String messageKey) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
        player.sendSystemMessage(Component.translatable(messageKey));
    }
}