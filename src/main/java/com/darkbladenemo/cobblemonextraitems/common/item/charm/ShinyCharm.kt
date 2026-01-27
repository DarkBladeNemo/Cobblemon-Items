package com.darkbladenemo.cobblemonextraitems.common.item.charm

import com.darkbladenemo.cobblemonextraitems.common.config.Config
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import top.theillusivec4.curios.api.type.capability.ICurioItem

class ShinyCharm : Item(Properties()
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

        val data = stack.get(ModDataComponents.SHINY_CHARM_DATA.get())
        val multiplier = data?.multiplier() ?: Config.SHINY_CHARM_MULTIPLIER.get().toFloat()

        tooltipComponents.add(Component.translatable("item.cobblemonextraitems.shiny_charm.tooltip"))
        tooltipComponents.add(Component.translatable("tooltip.cobblemonextraitems.shiny_boost",
            String.format("%.1f", multiplier)))
    }
}