package com.darkbladenemo.cobblemonextraitems.item

import com.cobblemon.mod.common.CobblemonSounds
import com.cobblemon.mod.common.api.item.PokemonSelectingItem
import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.item.CobblemonItem
import com.cobblemon.mod.common.pokemon.IVs
import com.cobblemon.mod.common.pokemon.Pokemon
import com.darkbladenemo.cobblemonextraitems.config.Config
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class HighHyperTrainingItem(
    val targetStats: Set<Stat>
) : CobblemonItem(Properties()), PokemonSelectingItem {

    override val bagItem = null

    // Get config value at runtime
    private val ivIncreaseAmount: Int
        get() = Config.HIGH_IV_INCREASE_AMOUNT.get()

    private fun canChangeIV(stat: Stat, pokemon: Pokemon): Boolean {
        val effectiveIV = pokemon.ivs.getEffectiveBattleIV(stat)
        // Can always use if not already at max
        return effectiveIV < IVs.MAX_VALUE
    }

    override fun canUseOnPokemon(stack: ItemStack, pokemon: Pokemon): Boolean {
        return targetStats.any { stat -> canChangeIV(stat, pokemon) }
    }

    override fun applyToPokemon(
        player: ServerPlayer,
        stack: ItemStack,
        pokemon: Pokemon
    ): InteractionResultHolder<ItemStack> {
        if (!canUseOnPokemon(stack, pokemon)) {
            return InteractionResultHolder.fail(stack)
        }

        var anyChanged = false
        targetStats.forEach { stat ->
            if (canChangeIV(stat, pokemon)) {
                val effectiveIV = pokemon.ivs.getEffectiveBattleIV(stat)
                val newIV = (effectiveIV + ivIncreaseAmount).coerceAtMost(IVs.MAX_VALUE)

                // Only apply if there's actually a change
                if (newIV != effectiveIV) {
                    pokemon.hyperTrainIV(stat, newIV)
                    anyChanged = true
                }
            }
        }

        if (anyChanged) {
            stack.consume(1, player)
            pokemon.entity?.playSound(CobblemonSounds.MEDICINE_PILLS_USE, 1F, 1F)
            return InteractionResultHolder.success(stack)
        }

        return InteractionResultHolder.fail(stack)
    }

    override fun use(world: Level, user: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (user is ServerPlayer) {
            return use(user, user.getItemInHand(hand))
        }
        return InteractionResultHolder.success(user.getItemInHand(hand))
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)

        val statName = when(targetStats.firstOrNull()) {
            Stats.HP -> "HP"
            Stats.ATTACK -> "Attack"
            Stats.DEFENCE -> "Defence"
            Stats.SPECIAL_ATTACK -> "Special Attack"
            Stats.SPECIAL_DEFENCE -> "Special Defence"
            Stats.SPEED -> "Speed"
            else -> "IV"
        }

        tooltipComponents.add(Component.translatable("tooltip.cobblemonextraitems.iv_item",
            statName, ivIncreaseAmount))
    }
}