// MultiCharmSelectionScreen.kt
package com.darkbladenemo.cobblemonextraitems.client.screen

import com.darkbladenemo.cobblemonextraitems.component.MultiCharmData
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents
import com.darkbladenemo.cobblemonextraitems.init.ModItems
import com.darkbladenemo.cobblemonextraitems.network.OpenMultiCharmFromCurioPayload
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.PacketDistributor
import top.theillusivec4.curios.api.CuriosApi

class MultiCharmSelectionScreen(
    private val player: Player,
    private val slotIndices: List<Int>
) : Screen(Component.translatable("gui.cobblemonextraitems.multi_charm_selection.title")) {

    private val GUI_WIDTH = 176
    private val GUI_HEIGHT = 100 + (slotIndices.size * 25)

    private var leftPos = 0
    private var topPos = 0

    override fun init() {
        super.init()

        leftPos = (width - GUI_WIDTH) / 2
        topPos = (height - GUI_HEIGHT) / 2

        CuriosApi.getCuriosInventory(player).ifPresent { inventory ->
            val slots = inventory.findCurios("type_charm_slot")

            slotIndices.forEachIndexed { index, slotIndex ->
                if (slotIndex < slots.size) {
                    val stack = slots[slotIndex].stack()
                    if (stack.`is`(ModItems.MULTI_CHARM.get())) {
                        val data = stack.get(ModDataComponents.MULTI_CHARM_DATA.get()) ?: MultiCharmData.empty()
                        val typeCount = data.typeEffects().size

                        val buttonY = topPos + 30 + (index * 25)

                        val buttonLabel = "Multi-Charm #${index + 1} ($typeCount types)"

                        addRenderableWidget(
                            Button.builder(
                                Component.literal(buttonLabel)
                            ) { _ ->
                                PacketDistributor.sendToServer(OpenMultiCharmFromCurioPayload(slotIndex))
                                onClose()
                            }
                                .bounds(leftPos + 20, buttonY, GUI_WIDTH - 40, 20)
                                .build()
                        )
                    }
                }
            }
        }
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick)
        super.render(guiGraphics, mouseX, mouseY, partialTick)

        guiGraphics.drawString(
            font,
            title,
            leftPos + 8,
            topPos + 10,
            0x404040,
            false
        )

        guiGraphics.drawString(
            font,
            Component.translatable("gui.cobblemonextraitems.multi_charm_selection.subtitle"),
            leftPos + 8,
            topPos + 20,
            0x808080,
            false
        )
    }

    override fun isPauseScreen(): Boolean = false
}