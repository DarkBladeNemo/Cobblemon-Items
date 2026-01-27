package com.darkbladenemo.cobblemonextraitems.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ShinyCharmData(float multiplier) {
    public static final Codec<ShinyCharmData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("multiplier").forGetter(ShinyCharmData::multiplier)
            ).apply(instance, ShinyCharmData::new)
    );
}