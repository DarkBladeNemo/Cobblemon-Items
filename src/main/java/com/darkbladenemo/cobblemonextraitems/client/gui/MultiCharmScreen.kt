package com.darkbladenemo.cobblemonextraitems.client.gui

import com.darkbladenemo.cobblemonextraitems.CobblemonExtraItemsMod
import com.darkbladenemo.cobblemonextraitems.common.component.MultiCharmData
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents
import com.darkbladenemo.cobblemonextraitems.common.item.charm.CharmType
import com.darkbladenemo.cobblemonextraitems.network.payload.ToggleMultiCharmTypePayload
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.network.PacketDistributor
import top.theillusivec4.curios.api.CuriosApi

class MultiCharmButton(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    message: Component,
    private var hasType: Boolean,
    private var isEnabled: Boolean,
    onPress: OnPress
) : Button(x, y, width, height, message, onPress, DEFAULT_NARRATION) {

    companion object {
        private val TEXTURE_DISABLED = ResourceLocation.fromNamespaceAndPath(
            CobblemonExtraItemsMod.MOD_ID,
            "textures/gui/multicharm_button_disabled.png"
        )
        private val TEXTURE_ENABLED = ResourceLocation.fromNamespaceAndPath(
            CobblemonExtraItemsMod.MOD_ID,
            "textures/gui/multicharm_button_effect_enabled.png"
        )
        private val TEXTURE_EFFECT_DISABLED = ResourceLocation.fromNamespaceAndPath(
            CobblemonExtraItemsMod.MOD_ID,
            "textures/gui/multicharm_button_effect_disabled.png"
        )
    }

    fun updateState(hasType: Boolean, isEnabled: Boolean) {
        this.hasType = hasType
        this.isEnabled = isEnabled
        this.active = hasType
    }

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        val texture = when {
            !hasType -> TEXTURE_DISABLED
            isEnabled -> TEXTURE_ENABLED
            else -> TEXTURE_EFFECT_DISABLED
        }

        val vOffset = if (hasType && isHovered) 20 else 0

        guiGraphics.blit(
            texture,
            x, y,
            0f, vOffset.toFloat(),
            width, height,
            72, 40
        )

        val textColor = if (hasType) 0xFFFFFF else 0xA0A0A0
        guiGraphics.drawCenteredString(
            Minecraft.getInstance().font,
            message,
            x + width / 2,
            y + (height - 8) / 2,
            textColor
        )
    }

    override fun playDownSound(soundManager: net.minecraft.client.sounds.SoundManager) {
        if (hasType) {
            super.playDownSound(soundManager)
        }
    }
}

class MultiCharmScreen(
    private val player: Player,
    private val curioSlotIndex: Int = -1,
    private val fromCurio: Boolean = false
) : Screen(Component.translatable("gui.cobblemonextraitems.multi_charm.title")) {

    private val backgroundTexture = ResourceLocation.fromNamespaceAndPath(CobblemonExtraItemsMod.MOD_ID, "textures/gui/multi_charm.png")
    private val backgroundWidth = 176
    private val backgroundHeight = 264

    private var leftPos = 0
    private var topPos = 0
    private var multiCharmStack: ItemStack = ItemStack.EMPTY
    private var currentData: MultiCharmData = MultiCharmData.empty()
    private val typeButtons = mutableMapOf<CharmType, MultiCharmButton>()

    override fun init() {
        super.init()

        leftPos = (width - backgroundWidth) / 2
        topPos = (height - backgroundHeight) / 2

        multiCharmStack = if (fromCurio && curioSlotIndex >= 0) {
            var stack = ItemStack.EMPTY
            CuriosApi.getCuriosInventory(player).ifPresent { inventory ->
                val slots = inventory.findCurios("type_charm_slot")
                if (curioSlotIndex < slots.size) {
                    stack = slots[curioSlotIndex].stack()
                }
            }
            stack
        } else {
            when {
                player.mainHandItem.item is com.darkbladenemo.cobblemonextraitems.common.item.charm.MultiCharm -> player.mainHandItem
                player.offhandItem.item is com.darkbladenemo.cobblemonextraitems.common.item.charm.MultiCharm -> player.offhandItem
                else -> ItemStack.EMPTY
            }
        }

        if (multiCharmStack.isEmpty) {
            onClose()
            return
        }

        currentData = multiCharmStack.get(ModDataComponents.MULTI_CHARM_DATA.get()) ?: MultiCharmData.empty()

        createButtons()
    }

    private fun createButtons() {
        typeButtons.clear()

        val types = CharmType.entries.sortedBy { it.name }
        var row = 0
        var col = 0

        types.forEach { type ->
            val hasType = currentData.hasType(type)
            val isEnabled = currentData.isTypeEnabled(type)

            val buttonX = leftPos + 13 + (col * 78)
            val buttonY = topPos + 26 + (row * 24)

            val button = MultiCharmButton(
                x = buttonX,
                y = buttonY,
                width = 72,
                height = 20,
                message = getButtonLabel(type, hasType, isEnabled),
                hasType = hasType,
                isEnabled = isEnabled,
                onPress = { _ ->
                    if (hasType) {
                        PacketDistributor.sendToServer(
                            ToggleMultiCharmTypePayload(type.translationKey, curioSlotIndex, fromCurio)
                        )
                    }
                }
            )

            typeButtons[type] = button
            addRenderableWidget(button)

            col++
            if (col >= 2) {
                col = 0
                row++
            }
        }
    }

    fun refreshData(newData: MultiCharmData) {
        currentData = newData

        typeButtons.forEach { (type, button) ->
            val hasType = currentData.hasType(type)
            val isEnabled = currentData.isTypeEnabled(type)
            button.message = getButtonLabel(type, hasType, isEnabled)
            button.updateState(hasType, isEnabled)
        }
    }

    private fun getButtonLabel(type: CharmType, hasType: Boolean, isEnabled: Boolean): Component {
        val typeName = Component.translatable("cobblemon.type.${type.translationKey}")

        return when {
            !hasType -> Component.literal("§8").append(typeName)
            isEnabled -> typeName
            else -> typeName
        }
    }

    override fun renderBlurredBackground(delta: Float) {}
    override fun renderMenuBackground(guiGraphics: GuiGraphics){}

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        guiGraphics.blit(backgroundTexture, leftPos, topPos, 0f, 0f, backgroundWidth, backgroundHeight, 176, 264)
        super.render(guiGraphics, mouseX, mouseY, partialTick)

        val titleX = leftPos + (backgroundWidth / 2) - (font.width(title) / 2) + 1
        val titleY = topPos + 9

        guiGraphics.drawString(
            font,
            title,
            titleX,
            titleY,
            0x404040,
            false
        )
    }

    override fun isPauseScreen(): Boolean = false
}