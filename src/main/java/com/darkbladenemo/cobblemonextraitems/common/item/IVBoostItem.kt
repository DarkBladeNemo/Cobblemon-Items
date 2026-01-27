package com.darkbladenemo.cobblemonextraitems.common.item

import com.cobblemon.mod.common.CobblemonSounds
import com.cobblemon.mod.common.api.item.PokemonSelectingItem
import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.item.CobblemonItem
import com.cobblemon.mod.common.pokemon.IVs
import com.cobblemon.mod.common.pokemon.Pokemon
import com.darkbladenemo.cobblemonextraitems.common.config.Config
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

/**
 * Generic IV boosting item that can target one or more stats.
 * Used for both single-stat candies and multi-stat bottle caps.
 */
class IVBoostItem(
    val targetStats: Set<Stat>
) : CobblemonItem(Properties()), PokemonSelectingItem {

    override val bagItem = null

    private fun getIVAmount(stack: ItemStack): Int {
        val data = stack.get(ModDataComponents.IV_ITEM_DATA.get())
        return data?.ivAmount() ?: Config.HIGH_IV_INCREASE_AMOUNT.get()
    }

    private fun canChangeIV(stat: Stat, pokemon: Pokemon): Boolean {
        val effectiveIV = pokemon.ivs.getEffectiveBattleIV(stat)
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

        val ivIncreaseAmount = getIVAmount(stack)
        var anyChanged = false

        targetStats.forEach { stat ->
            if (canChangeIV(stat, pokemon)) {
                val effectiveIV = pokemon.ivs.getEffectiveBattleIV(stat)
                val newIV = (effectiveIV + ivIncreaseAmount).coerceAtMost(IVs.MAX_VALUE)

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

        val ivIncreaseAmount = getIVAmount(stack)

        if (targetStats.size == 6) {
            // Special handling for Gold Bottle Cap (all stats)
            tooltipComponents.add(
                Component.translatable("tooltip.cobblemonextraitems.bottle_cap_all",
                    ivIncreaseAmount)
            )
        } else if (targetStats.size > 1) {
            // Multiple stats (but not all 6)
            val statList = targetStats.joinToString(", ") { stat ->
                when(stat) {
                    Stats.HP -> "HP"
                    Stats.ATTACK -> "Atk"
                    Stats.DEFENCE -> "Def"
                    Stats.SPECIAL_ATTACK -> "SpA"
                    Stats.SPECIAL_DEFENCE -> "SpD"
                    Stats.SPEED -> "Spe"
                    else -> "?"
                }
            }
            tooltipComponents.add(
                Component.translatable("tooltip.cobblemonextraitems.iv_item_multi",
                    statList, ivIncreaseAmount)
            )
        } else {
            // Single stat
            val statName = when(targetStats.firstOrNull()) {
                Stats.HP -> "HP"
                Stats.ATTACK -> "Attack"
                Stats.DEFENCE -> "Defence"
                Stats.SPECIAL_ATTACK -> "Special Attack"
                Stats.SPECIAL_DEFENCE -> "Special Defence"
                Stats.SPEED -> "Speed"
                else -> "IV"
            }

            tooltipComponents.add(
                Component.translatable("tooltip.cobblemonextraitems.iv_item",
                    statName, ivIncreaseAmount)
            )
        }
    }
}