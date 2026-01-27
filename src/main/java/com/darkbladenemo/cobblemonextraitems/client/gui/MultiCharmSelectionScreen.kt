package com.darkbladenemo.cobblemonextraitems.client.gui

import com.darkbladenemo.cobblemonextraitems.common.component.MultiCharmData
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents
import com.darkbladenemo.cobblemonextraitems.init.ModItems
import com.darkbladenemo.cobblemonextraitems.network.payload.OpenMultiCharmFromCurioPayload
import net.minecraft.ChatFormatting
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

    private val GUI_WIDTH = 220
    private val BUTTON_SECTION_HEIGHT = 30 + (slotIndices.size * 25)
    private val ACTIVE_TYPES_SECTION_HEIGHT = 60 + (slotIndices.size * 30) // Adjust based on content
    private val GUI_HEIGHT = BUTTON_SECTION_HEIGHT + ACTIVE_TYPES_SECTION_HEIGHT + 20

    private var leftPos = 0
    private var topPos = 0
    private val charmDataList = mutableListOf<Pair<Int, MultiCharmData>>() // slotIndex to data

    override fun init() {
        super.init()

        leftPos = (width - GUI_WIDTH) / 2
        topPos = (height - GUI_HEIGHT) / 2

        // Collect charm data for display
        charmDataList.clear()

        CuriosApi.getCuriosInventory(player).ifPresent { inventory ->
            val slots = inventory.findCurios("type_charm_slot")

            slotIndices.forEachIndexed { index, slotIndex ->
                if (slotIndex < slots.size) {
                    val stack = slots[slotIndex].stack()
                    if (stack.`is`(ModItems.MULTI_CHARM.get())) {
                        val data = stack.get(ModDataComponents.MULTI_CHARM_DATA.get()) ?: MultiCharmData.empty()
                        charmDataList.add(Pair(slotIndex, data))

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

        // Render title
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

        // Render active boosts section
        val activeSectionY = topPos + BUTTON_SECTION_HEIGHT + 10

        // Draw separator line
        guiGraphics.fill(leftPos + 10, activeSectionY - 5, leftPos + GUI_WIDTH - 10, activeSectionY - 4, 0xFF606060.toInt())

        // Active Boosts title
        guiGraphics.drawString(
            font,
            Component.translatable("gui.cobblemonextraitems.multi_charm_selection.active_boosts")
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD),
            leftPos + 10,
            activeSectionY,
            0xFFAA00,
            false
        )

        // Render each charm's active types
        var yOffset = activeSectionY + 15
        charmDataList.forEachIndexed { index, (slotIndex, data) ->
            val enabledTypes = data.getEnabledEffects()

            if (enabledTypes.isEmpty()) {
                // Show "No active types" message
                guiGraphics.drawString(
                    font,
                    Component.literal("Multi-Charm #${index + 1}: ")
                        .withStyle(ChatFormatting.GRAY),
                    leftPos + 15,
                    yOffset,
                    0x808080,
                    false
                )

                guiGraphics.drawString(
                    font,
                    Component.translatable("gui.cobblemonextraitems.multi_charm_selection.no_active")
                        .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC),
                    leftPos + 105,
                    yOffset,
                    0x606060,
                    false
                )
            } else {
                // Show charm number
                guiGraphics.drawString(
                    font,
                    Component.literal("Multi-Charm #${index + 1}:")
                        .withStyle(ChatFormatting.AQUA),
                    leftPos + 15,
                    yOffset,
                    0x55FFFF,
                    false
                )

                // Show active types
                val typeNames = enabledTypes.keys.sortedBy { it.name }.joinToString(", ") { type ->
                    type.translationKey.replaceFirstChar { it.uppercase() }
                }

                // Word wrap if needed
                val maxWidth = GUI_WIDTH - 110
                val wrappedText = wrapText(typeNames, maxWidth)

                wrappedText.forEachIndexed { lineIndex, line ->
                    guiGraphics.drawString(
                        font,
                        Component.literal(line).withStyle(ChatFormatting.GREEN),
                        leftPos + 105,
                        yOffset + (lineIndex * 10),
                        0x55FF55,
                        false
                    )
                }

                yOffset += wrappedText.size * 10
            }

            yOffset += 15
        }
    }

    private fun wrapText(text: String, maxWidth: Int): List<String> {
        val words = text.split(", ")
        val lines = mutableListOf<String>()
        var currentLine = ""

        words.forEach { word ->
            val testLine = if (currentLine.isEmpty()) word else "$currentLine, $word"
            val width = font.width(testLine)

            if (width > maxWidth && currentLine.isNotEmpty()) {
                lines.add(currentLine)
                currentLine = word
            } else {
                currentLine = testLine
            }
        }

        if (currentLine.isNotEmpty()) {
            lines.add(currentLine)
        }

        return lines.ifEmpty { listOf(text) }
    }

    override fun isPauseScreen(): Boolean = false
}