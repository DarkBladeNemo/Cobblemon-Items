package com.darkbladenemo.cobblemonextraitems.item.charm

import com.darkbladenemo.cobblemonextraitems.component.ExpCharmData
import com.darkbladenemo.cobblemonextraitems.config.Config
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import top.theillusivec4.curios.api.type.capability.ICurioItem

class ExpCharm : Item(Properties()
    .stacksTo(1)
    .rarity(Rarity.UNCOMMON)
), ICurioItem {

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)

        val data = stack.get(ModDataComponents.EXP_CHARM_DATA.get())
        val multiplier = data?.multiplier() ?: 1.5f

        tooltipComponents.add(Component.translatable("item.cobblemonextraitems.exp_charm.tooltip"))
        tooltipComponents.add(
            Component.translatable(
                "tooltip.cobblemonextraitems.exp_boost",
                String.format("%.0f", (multiplier - 1.0) * 100)
            )
        )
        tooltipComponents.add(Component.literal("§7Current: §a${String.format("%.1f", multiplier)}x"))
    }
}