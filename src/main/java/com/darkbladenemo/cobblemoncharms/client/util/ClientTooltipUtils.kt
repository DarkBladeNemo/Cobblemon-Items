package com.darkbladenemo.cobblemoncharms.client.util

import com.darkbladenemo.cobblemoncharms.CobblemonCharmsMod
import com.darkbladenemo.cobblemoncharms.common.config.Config
import com.darkbladenemo.cobblemoncharms.common.item.charm.CharmType
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

/**
 * Client-side helpers for charm tooltip state.
 * Safe to call from appendHoverText, which is client-only.
 */
object ClientTooltipUtils {

    fun hasAdvancement(path: String): Boolean {
        val id = ResourceLocation.fromNamespaceAndPath(CobblemonCharmsMod.MOD_ID, path)
        return ClientAdvancementCache.has(id)
    }

    fun isTypeCharmEnabled(type: CharmType): Boolean = Config.isTypeCharmEnabled(type)

    /**
     * Appends a status line to the tooltip based on config and advancement state.
     * Returns true if the charm is fully active (no warning appended).
     */
    fun appendStatusTooltip(
        tooltipComponents: MutableList<Component>,
        enabled: Boolean,
        advancementPath: String,
        advancementLabel: Any
    ): Boolean {
        if (!enabled) {
            tooltipComponents.add(
                Component.translatable("item.cobblemoncharms.status.disabled_in_config")
                    .withStyle(ChatFormatting.RED)
            )
            return false
        }
        if (Config.CHARM_EFFECT_REQUIRES_ADVANCEMENT.get() && !hasAdvancement(advancementPath)) {
            tooltipComponents.add(
                Component.translatable(
                    "item.cobblemoncharms.status.requires_advancement",
                    advancementLabel
                )
            )
            return false
        }
        return true
    }
}
