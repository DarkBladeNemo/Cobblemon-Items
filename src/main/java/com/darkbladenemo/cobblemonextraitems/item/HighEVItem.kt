package com.darkbladenemo.cobblemonextraitems.item

import com.cobblemon.mod.common.CobblemonSounds
import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.item.interactive.EVIncreaseItem
import com.cobblemon.mod.common.pokemon.Pokemon
import com.darkbladenemo.cobblemonextraitems.component.EVItemData
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class HighEVItem(private val targetStat: Stat) : EVIncreaseItem(targetStat, 1) {
    override val sound: SoundEvent = CobblemonSounds.MEDICINE_PILLS_USE

    // Get EV amount from data component at runtime
    private fun getEVAmount(stack: ItemStack): Int {
        val data = stack.get(ModDataComponents.EV_ITEM_DATA.get())
        return data?.evAmount() ?: 100
    }

    // Override to use DataComponent value instead of constructor value
    override fun applyToPokemon(
        player: ServerPlayer,
        stack: ItemStack,
        pokemon: Pokemon
    ): InteractionResultHolder<ItemStack> {
        val evAmount = getEVAmount(stack)

        // Apply the EV increase using the DataComponent value
        val evStore = pokemon.evs
        val currentEV = evStore.getOrDefault(stat)

        // Cobblemon's max EV per stat is 252, total max is 510
        val newEV = (currentEV + evAmount).coerceAtMost(252)

        if (newEV != currentEV) {
            evStore[stat] = newEV
            stack.consume(1, player)
            pokemon.entity?.playSound(sound, 1F, 1F)
            return InteractionResultHolder.success(stack)
        }

        return InteractionResultHolder.fail(stack)
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)

        val evAmount = getEVAmount(stack)
        val statName = when(targetStat) {
            Stats.HP -> "HP"
            Stats.ATTACK -> "Attack"
            Stats.DEFENCE -> "Defence"
            Stats.SPECIAL_ATTACK -> "Sp. Atk"
            Stats.SPECIAL_DEFENCE -> "Sp. Def"
            Stats.SPEED -> "Speed"
            else -> "EV"
        }

        tooltipComponents.add(Component.translatable("tooltip.cobblemonextraitems.ev_item",
            statName, evAmount))
    }
}