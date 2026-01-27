package com.darkbladenemo.cobblemonextraitems.common.component;

import com.darkbladenemo.cobblemonextraitems.common.item.charm.CharmType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record TypeCharmData(
        CharmType type,
        float multiplier,
        double radius
) {
    public static final Codec<TypeCharmData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.xmap(
                            CharmType::fromString,
                            CharmType::getTranslationKey
                    ).fieldOf("type").forGetter(TypeCharmData::type),
                    Codec.FLOAT.fieldOf("multiplier").forGetter(TypeCharmData::multiplier),
                    Codec.DOUBLE.fieldOf("radius").forGetter(TypeCharmData::radius)
            ).apply(instance, TypeCharmData::new)
    );
}