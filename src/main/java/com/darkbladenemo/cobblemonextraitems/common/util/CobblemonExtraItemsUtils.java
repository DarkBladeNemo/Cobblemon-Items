package com.darkbladenemo.cobblemonextraitems.common.util;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CobblemonExtraItemsUtils {
    /**
     * Resolves the {@link Species} from a {@link PokemonSpawnDetail}.
     * Follows CobbleCuisine's pattern for consistency.
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

        // Handle both "cobblemon:pikachu" and "pikachu" formats
        ResourceLocation resourceLocation = speciesName.contains(":")
                ? ResourceLocation.parse(speciesName)
                : ResourceLocation.fromNamespaceAndPath("cobblemon", speciesName);

        return PokemonSpecies.INSTANCE.getByIdentifier(resourceLocation);
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
        if (primary != null) {
            consumer.accept(primary);
        }

        ElementalType secondary = species.getSecondaryType();
        if (secondary != null) {
            consumer.accept(secondary);
        }
    }

    /**
     * Checks if a species has a specific elemental type.
     *
     * @param species The Pokémon species.
     * @param type The ElementalType to check.
     * @return True if the species has this type.
     */
    public static boolean speciesHasType(Species species, ElementalType type) {
        if (species == null || type == null) {
            return false;
        }

        ElementalType primary = species.getPrimaryType();
        ElementalType secondary = species.getSecondaryType();

        return type.equals(primary) || type.equals(secondary);
    }
}