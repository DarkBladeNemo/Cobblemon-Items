package com.darkbladenemo.cobblemonextraitems.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record IVItemData(
        List<String> targetStats,  // Store stat names as strings
        int ivAmount
) {
    public static final Codec<IVItemData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.listOf().fieldOf("stats").forGetter(IVItemData::targetStats),
                    Codec.INT.fieldOf("iv_amount").forGetter(IVItemData::ivAmount)
            ).apply(instance, IVItemData::new)
    );
}