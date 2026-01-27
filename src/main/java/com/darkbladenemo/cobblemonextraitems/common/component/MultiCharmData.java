package com.darkbladenemo.cobblemonextraitems.common.component;

import com.darkbladenemo.cobblemonextraitems.common.item.charm.CharmType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * Adds a type effect to this multi-charm
     * @return New MultiCharmData with the added effect, or the same if type already exists
     */
    public MultiCharmData addType(CharmType type, float multiplier, double radius) {
        if (typeEffects.containsKey(type)) {
            return this; // Type already exists, don't add
        }

        Map<CharmType, TypeEffect> newEffects = new HashMap<>(typeEffects);
        newEffects.put(type, new TypeEffect(multiplier, radius, true));
        return new MultiCharmData(newEffects);
    }

    /**
     * Toggles the enabled state of a type effect
     * @return New MultiCharmData with toggled state, or same if type doesn't exist
     */
    public MultiCharmData toggleType(CharmType type) {
        if (!typeEffects.containsKey(type)) {
            return this;
        }

        Map<CharmType, TypeEffect> newEffects = new HashMap<>(typeEffects);
        TypeEffect current = newEffects.get(type);
        newEffects.put(type, new TypeEffect(current.multiplier(), current.radius(), !current.enabled()));
        return new MultiCharmData(newEffects);
    }

    /**
     * Gets all enabled type effects
     */
    public Map<CharmType, TypeEffect> getEnabledEffects() {
        return typeEffects.entrySet().stream()
                .filter(entry -> entry.getValue().enabled())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Checks if a type is present (regardless of enabled state)
     */
    public boolean hasType(CharmType type) {
        return typeEffects.containsKey(type);
    }

    /**
     * Checks if a type is enabled
     */
    public boolean isTypeEnabled(CharmType type) {
        TypeEffect effect = typeEffects.get(type);
        return effect != null && effect.enabled();
    }

    public record TypeEffect(
            float multiplier,
            double radius,
            boolean enabled
    ) {
        public static final Codec<TypeEffect> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.FLOAT.fieldOf("multiplier").forGetter(TypeEffect::multiplier),
                        Codec.DOUBLE.fieldOf("radius").forGetter(TypeEffect::radius),
                        Codec.BOOL.fieldOf("enabled").forGetter(TypeEffect::enabled)
                ).apply(instance, TypeEffect::new)
        );
    }
}