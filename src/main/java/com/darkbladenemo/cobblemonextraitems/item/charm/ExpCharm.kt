package com.darkbladenemo.cobblemonextraitems.item.charm

import com.darkbladenemo.cobblemonextraitems.config.Config
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
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

        val multiplier = Config.EXP_CHARM_MULTIPLIER.get()
        tooltipComponents.add(Component.translatable("item.cobblemonextraitems.exp_charm.tooltip"))
        tooltipComponents.add(
            Component.translatable(
                "tooltip.cobblemonextraitems.exp_boost",
                String.format("%.0f", (multiplier - 1.0) * 100)
            )
        )
    }
}