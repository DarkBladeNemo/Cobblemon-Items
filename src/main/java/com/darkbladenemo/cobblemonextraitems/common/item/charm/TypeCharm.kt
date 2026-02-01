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
        val matchMultiplier = data?.matchMultiplier() ?: Config.TYPE_CHARM_MATCH_MULTIPLIER.get().toFloat()

        // Main effect description
        tooltipComponents.add(
            Component.translatable(
                "item.cobblemonextraitems.${type.translationKey}_charm.tooltip",
                String.format("%.1f", matchMultiplier)
            )
        )

        // Show global penalty if it's enabled (< 1.0)
        val globalPenalty = Config.TYPE_CHARM_NON_MATCH_MULTIPLIER.get().toFloat()
        if (globalPenalty < 1.0f) {
            val penaltyPercent = (1.0f - globalPenalty) * 100
            tooltipComponents.add(
                Component.literal("§7Other types: §c-${String.format("%.0f", penaltyPercent)}%")
            )
        }

        // Show global radius (same for all type charms)
        val globalRadius = Config.TYPE_CHARM_RADIUS.get().toInt()
        tooltipComponents.add(Component.literal("§7Radius: §a$globalRadius blocks"))
    }

    override fun getName(stack: ItemStack): Component =
        Component.translatable("item.cobblemonextraitems.${type.translationKey}_charm")
}