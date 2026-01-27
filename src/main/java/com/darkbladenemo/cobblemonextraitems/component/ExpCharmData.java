package com.darkbladenemo.cobblemonextraitems.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ExpCharmData(float multiplier) {
    public static final Codec<ExpCharmData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("multiplier").forGetter(ExpCharmData::multiplier)
            ).apply(instance, ExpCharmData::new)
    );
}