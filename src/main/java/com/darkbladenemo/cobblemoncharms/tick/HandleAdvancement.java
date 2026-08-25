package com.darkbladenemo.cobblemoncharms.tick;

import com.darkbladenemo.cobblemoncharms.advancement.ModAdvancement;
import com.darkbladenemo.cobblemoncharms.common.config.Config;
import com.darkbladenemo.cobblemoncharms.common.event.TypeCharmAdvancementEvents;
import com.darkbladenemo.cobblemoncharms.init.ModItems;
import com.darkbladenemo.cobblemoncharms.utils.AdvancementUtils;
import com.darkbladenemo.cobblemoncharms.utils.PokedexRegionUtils;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class HandleAdvancement {

    public static void checkAdvancements(MinecraftServer server) {
        server.getPlayerList().getPlayers().forEach(player -> {
            grantRootAdvancement(player);
            checkShinyCharmThreshold(player);
            checkRegionCompletions(player);
            TypeCharmAdvancementEvents.checkAllTypeCharmsForPlayer(player);
        });
    }

    private static void checkRegionCompletions(ServerPlayer player) {
        for (ModAdvancement adv : ModAdvancement.values()) {
            String region = adv.getRegionKey();
            if (region == null || region.equals("national")) continue;
            if (!PokedexRegionUtils.isRegionCompleted(player, region)) continue;
            AdvancementUtils.grantAdvancement(player, adv.getAdvancement(player.serverLevel()));
        }
    }

    private static void grantRootAdvancement(ServerPlayer player) {
        AdvancementHolder root = ModAdvancement.ROOT.getAdvancement(player.serverLevel());
        AdvancementUtils.grantAdvancement(player, root);
    }

    private static void checkShinyCharmThreshold(ServerPlayer player) {
        if (!Config.ENABLE_SHINY_CHARM.get()) return;
        double threshold = Config.SHINY_CHARM_DEX_THRESHOLD.get();

        if (!PokedexRegionUtils.hasReachedThreshold(player, "national", threshold)) return;

        AdvancementHolder shinyCharmAdvancement =
                ModAdvancement.SHINY_CHARM.getAdvancement(player.serverLevel());

        boolean newlyGranted = AdvancementUtils.grantAdvancement(player, shinyCharmAdvancement);
        if (newlyGranted && Config.GRANT_CHARM_ON_ADVANCEMENT.get() && Config.GRANT_SHINY_CHARM_ON_ADVANCEMENT.get()) {
            giveShinyCharm(player, threshold);
        }
    }

    private static void giveShinyCharm(ServerPlayer player, double threshold) {
        ItemStack charm = new ItemStack(ModItems.SHINY_CHARM.get());

        if (!player.getInventory().add(charm)) {
            player.drop(charm, false);
        }

        if (threshold >= 100.0) {
            player.sendSystemMessage(
                    Component.translatable("message.cobblemoncharms.shiny_charm_awarded_full")
            );
        } else {
            player.sendSystemMessage(
                    Component.translatable("message.cobblemoncharms.shiny_charm_awarded_threshold",
                            String.format("%.0f", threshold))
            );
        }
    }
}