package com.darkbladenemo.cobblemoncharms.common.util;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class CobblemonCharmsUtils {
    /**
     * Cache for species resolution to avoid repeated registry lookups.
     * Species definitions don't change at runtime, so this is safe.
     * Using ConcurrentHashMap for thread-safety during world gen.
     */
    private static final Map<String, Species> SPECIES_CACHE = new ConcurrentHashMap<>();

    /**
     * Resolves the {@link Species} from a {@link PokemonSpawnDetail}.
     * Uses a cache to avoid repeated registry lookups for the same species.
     *
     * @param detail The PokemonSpawnDetail instance.
     * @return The resolved {@link Species}, or {@code null} if unspecified.
     */
    @Nullable
    public static Species resolveSpecies(PokemonSpawnDetail detail) {
        String speciesName = detail.getPokemon().getSpecies();
        if (speciesName == null) {
            return null;
        }

        // Check cache first
        return SPECIES_CACHE.computeIfAbsent(speciesName, name -> {
            // Handle both "cobblemon:pikachu" and "pikachu" formats
            ResourceLocation resourceLocation = name.contains(":")
                    ? ResourceLocation.parse(name)
                    : ResourceLocation.fromNamespaceAndPath("cobblemon", name);

            return PokemonSpecies.getByIdentifier(resourceLocation);
        });
    }

    /**
     * Iterates over each ElementalType of a species without allocations.
     * Uses Cobblemon's native API for better performance.
     *
     * @param species The Pokémon species.
     * @param consumer Consumer to process each type.
     */
    public static void forEachType(Species species, Consumer<ElementalType> consumer) {
        if (species == null) {
            return;
        }

        ElementalType primary = species.getPrimaryType();
        consumer.accept(primary);

        ElementalType secondary = species.getSecondaryType();
        if (secondary != null) {
            consumer.accept(secondary);
        }
    }
}
