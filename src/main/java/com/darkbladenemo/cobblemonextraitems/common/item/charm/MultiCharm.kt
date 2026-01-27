package com.darkbladenemo.cobblemonextraitems.common.item.charm

import com.darkbladenemo.cobblemonextraitems.common.component.MultiCharmData
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents
import com.darkbladenemo.cobblemonextraitems.network.payload.OpenMultiCharmScreenPayload
import net.minecraft.ChatFormatting
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
import net.neoforged.neoforge.network.PacketDistributor
import top.theillusivec4.curios.api.type.capability.ICurioItem

class MultiCharm : Item(
    Properties()
        .stacksTo(1)
        .rarity(Rarity.RARE)
), ICurioItem {

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(usedHand)

        if (player.isShiftKeyDown && !level.isClientSide) {
            // Open GUI on shift-right-click
            if (player is ServerPlayer) {
                PacketDistributor.sendToPlayer(player, OpenMultiCharmScreenPayload())
            }
            return InteractionResultHolder.success(stack)
        }

        return InteractionResultHolder.pass(stack)
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)

        val data = stack.get(ModDataComponents.MULTI_CHARM_DATA.get()) ?: MultiCharmData.empty()

        tooltipComponents.add(
            Component.translatable("item.cobblemonextraitems.multi_charm.tooltip")
                .withStyle(ChatFormatting.GRAY)
        )

        if (data.typeEffects().isEmpty()) {
            tooltipComponents.add(
                Component.translatable("item.cobblemonextraitems.multi_charm.empty")
                    .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
            )
        } else {
            tooltipComponents.add(
                Component.translatable("item.cobblemonextraitems.multi_charm.types")
                    .withStyle(ChatFormatting.GOLD)
            )

            data.typeEffects().entries.sortedBy { it.key.name }.forEach { (type, effect) ->
                val status = if (effect.enabled()) {
                    Component.translatable("item.cobblemonextraitems.multi_charm.enabled")
                        .withStyle(ChatFormatting.GREEN)
                } else {
                    Component.translatable("item.cobblemonextraitems.multi_charm.disabled")
                        .withStyle(ChatFormatting.RED)
                }

                tooltipComponents.add(
                    Component.literal("  ")
                        .append(Component.translatable("cobblemon.type.${type.translationKey}"))
                        .append(Component.literal(" - "))
                        .append(status)
                )
            }

            tooltipComponents.add(Component.empty())
            tooltipComponents.add(
                Component.translatable("item.cobblemonextraitems.multi_charm.hint")
                    .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
            )
        }
    }

    override fun getName(stack: ItemStack): Component =
        Component.translatable("item.cobblemonextraitems.multi_charm")
}