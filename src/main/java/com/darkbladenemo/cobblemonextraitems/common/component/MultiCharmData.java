package com.darkbladenemo.cobblemonextraitems.common.component;

import com.darkbladenemo.cobblemonextraitems.common.item.charm.CharmType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.*;

public record MultiCharmData(
        Map<CharmType, TypeEffect> typeEffects
) {
    public static final Codec<MultiCharmData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(
                            Codec.STRING.xmap(
                                    str -> {
                                        CharmType type = CharmType.fromString(str);
                                        if (type == null) {
                                            throw new IllegalArgumentException("Unknown charm type: " + str);
                                        }
                                        return type;
                                    },
                                    CharmType::getTranslationKey
                            ),
                            TypeEffect.CODEC
                    ).optionalFieldOf("type_effects", new HashMap<>()).forGetter(MultiCharmData::typeEffects)
            ).apply(instance, MultiCharmData::new)
    );

    public static MultiCharmData empty() {
        return new MultiCharmData(new HashMap<>());
    }

    public MultiCharmData addType(CharmType type, float matchMultiplier) {
        if (typeEffects.containsKey(type)) {
            return this;
        }

        Map<CharmType, TypeEffect> newEffects = new HashMap<>(typeEffects);
        newEffects.put(type, new TypeEffect(matchMultiplier, true));
        return new MultiCharmData(newEffects);
    }

    public MultiCharmData toggleType(CharmType type) {
        if (!typeEffects.containsKey(type)) {
            return this;
        }

        Map<CharmType, TypeEffect> newEffects = new HashMap<>(typeEffects);
        TypeEffect current = newEffects.get(type);
        newEffects.put(type, new TypeEffect(current.matchMultiplier(), !current.enabled()));
        return new MultiCharmData(newEffects);
    }

    /**
     * Gets all enabled type effects.
     */
    public Map<CharmType, TypeEffect> getEnabledEffects() {
        // Use EnumMap for better performance with enum keys
        Map<CharmType, TypeEffect> enabled = new EnumMap<>(CharmType.class);

        // Simple forEach - no stream overhead
        typeEffects.forEach((type, effect) -> {
            if (effect.enabled()) {
                enabled.put(type, effect);
            }
        });

        return enabled;
    }

    public boolean hasType(CharmType type) {
        return typeEffects.containsKey(type);
    }

    public boolean isTypeEnabled(CharmType type) {
        TypeEffect effect = typeEffects.get(type);
        return effect != null && effect.enabled();
    }

    public record TypeEffect(
            float matchMultiplier,
            boolean enabled
    ) {
        public static final Codec<TypeEffect> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.FLOAT.fieldOf("match_multiplier").forGetter(TypeEffect::matchMultiplier),
                        Codec.BOOL.fieldOf("enabled").forGetter(TypeEffect::enabled)
                ).apply(instance, TypeEffect::new)
        );
    }
}