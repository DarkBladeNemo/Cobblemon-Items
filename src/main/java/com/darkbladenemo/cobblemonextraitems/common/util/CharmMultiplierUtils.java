package com.darkbladenemo.cobblemonextraitems.common.util;

import com.darkbladenemo.cobblemonextraitems.common.component.ExpCharmData;
import com.darkbladenemo.cobblemonextraitems.common.component.ShinyCharmData;
import com.darkbladenemo.cobblemonextraitems.common.config.Config;
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents;
import com.darkbladenemo.cobblemonextraitems.init.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Function;

/**
 * Utility class for calculating charm multipliers from equipped Curio items.
 * Supports stacking - bonuses from multiple charms are added together.
 * Example: Two 1.5x charms = 1.0 + (0.5 + 0.5) = 2.0x total
 */
public class CharmMultiplierUtils {

    /**
     * Gets the EXP multiplier from equipped EXP Charms.
     * Multiple charms stack additively (bonuses add together).
     *
     * @param player The player to check
     * @return The final multiplier (1.0 if no charms equipped)
     */
    public static float getExpMultiplier(ServerPlayer player) {
        return getCharmMultiplier(
                player,
                "exp_charm_slot",
                ModItems.EXP_CHARM.get(),
                stack -> {
                    ExpCharmData data = stack.get(ModDataComponents.EXP_CHARM_DATA.get());
                    return data != null ? data.multiplier() : Config.EXP_CHARM_MULTIPLIER.get().floatValue();
                }
        );
    }

    /**
     * Gets the shiny chance multiplier from equipped Shiny Charms.
     * Multiple charms stack additively (bonuses add together).
     *
     * @param player The player to check
     * @return The final multiplier (1.0 if no charms equipped)
     */
    public static float getShinyMultiplier(ServerPlayer player) {
        return getCharmMultiplier(
                player,
                "shiny_charm_slot",
                ModItems.SHINY_CHARM.get(),
                stack -> {
                    ShinyCharmData data = stack.get(ModDataComponents.SHINY_CHARM_DATA.get());
                    return data != null ? data.multiplier() : Config.SHINY_CHARM_MULTIPLIER.get().floatValue();
                }
        );
    }

    /**
     * Generic method to calculate charm multipliers with additive stacking.
     * Example: 1.5x + 1.5x = 1.0 + 0.5 + 0.5 = 2.0x
     *
     * @param player The player to check
     * @param slotIdentifier The Curios slot identifier (e.g., "exp_charm_slot")
     * @param charmItem The charm item to look for
     * @param multiplierExtractor Function to extract multiplier from ItemStack
     * @return The final multiplier (1.0 if no charms found)
     */
    private static float getCharmMultiplier(
            ServerPlayer player,
            String slotIdentifier,
            Item charmItem,
            Function<ItemStack, Float> multiplierExtractor
    ) {
        return CuriosApi.getCuriosInventory(player)
                .map(inventory -> {
                    var slots = inventory.findCurios(slotIdentifier);

                    if (slots.isEmpty()) {
                        return 1.0f;
                    }

                    // Accumulate bonuses additively (like type charms)
                    float totalBonus = 0.0f;

                    for (var slotResult : slots) {
                        ItemStack stack = slotResult.stack();
                        if (!stack.isEmpty() && stack.is(charmItem)) {
                            float multiplier = multiplierExtractor.apply(stack);
                            // Add the bonus part (multiplier - 1.0)
                            totalBonus += (multiplier - 1.0f);
                        }
                    }

                    // Convert back to multiplier form
                    return 1.0f + totalBonus;
                })
                .orElse(1.0f);
    }

    /**
     * Counts how many charms of a specific type are equipped.
     * Useful for debugging or UI display.
     */
    public static int countEquippedCharms(ServerPlayer player, String slotIdentifier, Item charmItem) {
        return CuriosApi.getCuriosInventory(player)
                .map(inventory -> {
                    int count = 0;
                    for (var slotResult : inventory.findCurios(slotIdentifier)) {
                        ItemStack stack = slotResult.stack();
                        if (!stack.isEmpty() && stack.is(charmItem)) {
                            count++;
                        }
                    }
                    return count;
                })
                .orElse(0);
    }
}