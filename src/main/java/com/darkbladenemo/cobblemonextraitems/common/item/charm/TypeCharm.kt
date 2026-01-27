package com.darkbladenemo.cobblemonextraitems.common.item.charm

import com.darkbladenemo.cobblemonextraitems.common.config.Config
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents
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

    constructor(type: CharmType) : this(
        type,
        Properties()
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON)
    )

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)

        val data = stack.get(ModDataComponents.TYPE_CHARM_DATA.get())
        val multiplier = data?.multiplier() ?: Config.TYPE_CHARM_MULTIPLIER.get().toFloat()
        val radius = data?.radius() ?: Config.TYPE_CHARM_RADIUS.get()

        tooltipComponents.add(
            Component.translatable(
                "item.cobblemonextraitems.${type.translationKey}_charm.tooltip",
                String.format("%.1f", multiplier)
            )
        )

        tooltipComponents.add(Component.literal("§7Radius: §a${radius.toInt()} blocks"))
    }

    override fun getName(stack: ItemStack): Component =
        Component.translatable("item.cobblemonextraitems.${type.translationKey}_charm")
}