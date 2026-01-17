package com.darkbladenemo.cobblemonextraitems.item

import com.cobblemon.mod.common.CobblemonSounds
import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.item.interactive.EVIncreaseItem
import net.minecraft.sounds.SoundEvent

class HighEVItem(stat: Stat) : EVIncreaseItem(stat, 100) {
    override val sound: SoundEvent = CobblemonSounds.MEDICINE_PILLS_USE
}