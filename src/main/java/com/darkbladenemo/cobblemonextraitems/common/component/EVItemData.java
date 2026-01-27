package com.darkbladenemo.cobblemonextraitems.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record EVItemData(
        String statName,
        int evAmount
) {
    public static final Codec<EVItemData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("stat").forGetter(EVItemData::statName),
                    Codec.INT.fieldOf("ev_amount").forGetter(EVItemData::evAmount)
            ).apply(instance, EVItemData::new)
    );
}