package com.darkbladenemo.cobblemonextraitems.item

import com.cobblemon.mod.common.CobblemonSounds
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.item.interactive.EVIncreaseItem
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class HighEVItem(stat: Stats) : EVIncreaseItem(stat, 100) {
    override val sound: SoundEvent = CobblemonSounds.MEDICINE_PILLS_USE

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)

        val statName = when(stat) {
            Stats.HP -> "HP"
            Stats.ATTACK -> "Attack"
            Stats.DEFENCE -> "Defence"
            Stats.SPECIAL_ATTACK -> "Sp. Atk"
            Stats.SPECIAL_DEFENCE -> "Sp. Def"
            Stats.SPEED -> "Speed"
            else -> "EV"
        }

        tooltipComponents.add(Component.translatable("tooltip.cobblemonextraitems.ev_item",
            statName, 100))
    }
}