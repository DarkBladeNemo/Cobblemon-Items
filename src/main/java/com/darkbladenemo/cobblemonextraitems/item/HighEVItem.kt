package com.darkbladenemo.cobblemonextraitems.item;

import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.item.interactive.EVIncreaseItem;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class HighEVItem extends EVIncreaseItem {
    public HighEVItem(Stat stat) {
        super(stat, 100);
    }

    @Override
    public @NotNull SoundEvent getSound() {
        return CobblemonSounds.MEDICINE_PILLS_USE;
    }

    @Override
    public Void getBagItem() {
        return null;
    }
}