package com.darkbladenemo.cobblemonextraitems.item

import com.cobblemon.mod.common.CobblemonSounds
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.item.interactive.EVIncreaseItem
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class HighEVItem(stat: Stats) : EVIncreaseItem(stat, 100) {
    override val sound: SoundEvent = CobblemonSounds.MEDICINE_PILLS_USE

    override fun appendHoverText(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        // Call super first (if parent has any tooltips)
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)

        // Add our custom tooltip using the item's descriptionId
        // descriptionId is already "item.cobblemonextraitems.high_carbos" etc.
        val tooltipKey = "${stack.item.descriptionId}.tooltip"
        tooltipComponents.add(Component.translatable(tooltipKey))
    }
}