package com.darkbladenemo.cobblemonextraitems.item.charm

import com.darkbladenemo.cobblemonextraitems.config.Config
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import top.theillusivec4.curios.api.type.capability.ICurioItem

class TypeCharm(
    private val type: CharmType,
    properties: Properties = Properties()
        .stacksTo(1)
        .rarity(Rarity.UNCOMMON)
) : Item(properties), ICurioItem {

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)

        val multiplier = Config.TYPE_CHARM_MULTIPLIER.get()
        val radius = Config.TYPE_CHARM_RADIUS.get()

        tooltipComponents.add(
            Component.translatable(
                "item.cobblemonextraitems.${type.translationKey}_charm.tooltip",
                String.format("%.1f", multiplier)
            )
        )

        tooltipComponents.add(Component.literal("§7Radius: §a${radius.toInt()} blocks"))
    }

    // Optional: Override getName to make the display name dynamic
    override fun getName(stack: ItemStack): Component {
        return Component.translatable("item.cobblemonextraitems.${type.translationKey}_charm")
    }
}