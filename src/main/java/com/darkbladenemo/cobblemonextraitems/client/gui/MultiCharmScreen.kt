package com.darkbladenemo.cobblemonextraitems.client.gui

import com.darkbladenemo.cobblemonextraitems.CobblemonExtraItemsMod
import com.darkbladenemo.cobblemonextraitems.common.component.MultiCharmData
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents
import com.darkbladenemo.cobblemonextraitems.common.item.charm.CharmType
import com.darkbladenemo.cobblemonextraitems.network.payload.ToggleMultiCharmTypePayload
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.network.PacketDistributor
import top.theillusivec4.curios.api.CuriosApi

class MultiCharmScreen(
    private val player: Player,
    private val curioSlotIndex: Int = -1,
    private val fromCurio: Boolean = false
) : Screen(Component.translatable("gui.cobblemonextraitems.multi_charm.title")) {

    private val TEXTURE = ResourceLocation.fromNamespaceAndPath(CobblemonExtraItemsMod.MOD_ID, "textures/gui/multi_charm.png")
    private val GUI_WIDTH = 176
    private val GUI_HEIGHT = 256

    private var leftPos = 0
    private var topPos = 0
    private var multiCharmStack: ItemStack = ItemStack.EMPTY
    private var currentData: MultiCharmData = MultiCharmData.empty()
    private val typeButtons = mutableMapOf<CharmType, Button>()

    override fun init() {
        super.init()

        leftPos = (width - GUI_WIDTH) / 2
        topPos = (height - GUI_HEIGHT) / 2

        // Find multi-charm either from curio slot or hand
        multiCharmStack = if (fromCurio && curioSlotIndex >= 0) {
            // Get from curio slot
            var stack = ItemStack.EMPTY
            CuriosApi.getCuriosInventory(player).ifPresent { inventory ->
                val slots = inventory.findCurios("type_charm_slot")
                if (curioSlotIndex < slots.size) {
                    stack = slots[curioSlotIndex].stack()
                }
            }
            stack
        } else {
            // Get from hand (default behavior)
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

            val buttonX = leftPos + 10 + (col * 80)
            val buttonY = topPos + 20 + (row * 24)

            val button = Button.builder(getButtonLabel(type, hasType, isEnabled)) { _ ->
                if (hasType) {
                    // Send curio slot info along with toggle request
                    PacketDistributor.sendToServer(
                        ToggleMultiCharmTypePayload(type.translationKey, curioSlotIndex, fromCurio)
                    )
                }
            }
                .bounds(buttonX, buttonY, 76, 20)
                .build()

            // Disable button if type hasn't been added yet
            button.active = hasType

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

        // Update button labels and active states
        typeButtons.forEach { (type, button) ->
            val hasType = currentData.hasType(type)
            val isEnabled = currentData.isTypeEnabled(type)
            button.message = getButtonLabel(type, hasType, isEnabled)
            button.active = hasType
        }
    }

    private fun getButtonLabel(type: CharmType, hasType: Boolean, isEnabled: Boolean): Component {
        val typeName = Component.translatable("cobblemon.type.${type.translationKey}")

        return when {
            !hasType -> Component.literal("§8").append(typeName)
            isEnabled -> Component.literal("§a✓ ").append(typeName)
            else -> Component.literal("§c✗ ").append(typeName)
        }
    }

    override fun renderBlurredBackground(delta: Float) {}
    override fun renderMenuBackground(guiGraphics: GuiGraphics){}

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0f, 0f, GUI_WIDTH, GUI_HEIGHT, 176, 256)
        super.render(guiGraphics, mouseX, mouseY, partialTick)

        val titleX = leftPos + (GUI_WIDTH / 2) - (font.width(title) / 2)
        val titleY = topPos + 6

        // Render title
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