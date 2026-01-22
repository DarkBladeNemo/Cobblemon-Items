package com.darkbladenemo.cobblemonextraitems.item.charm

import com.darkbladenemo.cobblemonextraitems.config.Config
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import top.theillusivec4.curios.api.type.capability.ICurioItem

class RockCharm : Item(Properties()
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

        val multiplier = Config.TYPE_CHARM_MULTIPLIER.get()
        val radius = Config.TYPE_CHARM_RADIUS.get()

        tooltipComponents.add(Component.translatable("item.cobblemonextraitems.rock_charm.tooltip",
            String.format("%.1f", multiplier)))

        tooltipComponents.add(Component.literal("§7Radius: §a${radius.toInt()} blocks"))
    }
}