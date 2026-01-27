package com.darkbladenemo.cobblemonextraitems.common.util;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

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
        ResourceLocation resourceLocation = speciesName.indexOf(':') >= 0
                ? ResourceLocation.parse(speciesName)
                : ResourceLocation.fromNamespaceAndPath("cobblemon", speciesName);

        return PokemonSpecies.INSTANCE.getByIdentifier(resourceLocation);
    }

    /**
     * Gets the elemental types of a species as strings for easy comparison.
     * Useful for type charm matching without importing Cobblemon types everywhere.
     *
     * @param species The Pokémon species.
     * @return Array of type names (primary at index 0, secondary at index 1 if exists).
     */
    public static String[] getSpeciesTypeNames(Species species) {
        if (species == null) {
            return new String[0];
        }

        String primary = species.getPrimaryType() != null
                ? species.getPrimaryType().getName().toLowerCase()
                : null;
        String secondary = species.getSecondaryType() != null
                ? species.getSecondaryType().getName().toLowerCase()
                : null;

        if (secondary != null) {
            return new String[]{primary, secondary};
        } else {
            return new String[]{primary};
        }
    }

    /**
     * Checks if a species has a specific elemental type.
     *
     * @param species The Pokémon species.
     * @param typeName The type name to check (lowercase).
     * @return True if the species has this type.
     */
    public static boolean speciesHasType(Species species, String typeName) {
        if (species == null || typeName == null) {
            return false;
        }

        String[] types = getSpeciesTypeNames(species);
        for (String type : types) {
            if (typeName.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
}