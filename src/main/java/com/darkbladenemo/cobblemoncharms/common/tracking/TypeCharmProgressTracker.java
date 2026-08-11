package com.darkbladenemo.cobblemoncharms.common.tracking;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokedex.AbstractPokedexManager;
import com.cobblemon.mod.common.api.pokedex.FormDexRecord;
import com.cobblemon.mod.common.api.pokedex.PokedexEntryProgress;
import com.cobblemon.mod.common.api.pokedex.SpeciesDexRecord;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.pokemon.FormData;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.darkbladenemo.cobblemoncharms.common.item.charm.CharmType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

/**
 * Tracks how many unique species+form combinations of each type a player has caught.
 * <p>
 * Keys use "speciesPath:formName" (e.g. "diglett:Normal", "diglett:Alolan") so regional
 * forms count independently from their base form.
 */
public class TypeCharmProgressTracker {

    // Per-player: UUID -> CharmType -> set of "speciesPath:formName" keys
    private static final Map<UUID, Map<CharmType, Set<String>>> playerCache = new HashMap<>();

    // Global: CharmType -> count of unique implemented species+form entries (built once per session)
    private static Map<CharmType, Integer> implementedCountCache = null;

    public static int getUniqueCount(ServerPlayer player, CharmType type) {
        return getOrBuildPlayerCache(player).getOrDefault(type, Collections.emptySet()).size();
    }

    public static int getImplementedCount(CharmType type) {
        return getOrBuildImplementedCache().getOrDefault(type, 0);
    }

    public static int computeThreshold(CharmType type, double percentage) {
        int implemented = getImplementedCount(type);
        if (implemented == 0) return Integer.MAX_VALUE;
        return Math.max(1, (int) Math.ceil(implemented * percentage / 100.0));
    }

    /**
     * Records a caught Pokémon from the live event, crediting each of its form's types.
     * Returns {@code true} if any new species+form key was added to the cache.
     */
    public static boolean record(ServerPlayer player, Pokemon pokemon, FormDexRecord formRecord) {
        Map<CharmType, Set<String>> cache = getOrBuildPlayerCache(player);
        String key = pokemon.getSpecies().resourceIdentifier.getPath() + ":" + formRecord.getFormName();
        boolean anyNew = false;

        FormData form = pokemon.getSpecies().getFormByName(formRecord.getFormName());
        for (CharmType type : getTypesFromForm(form)) {
            if (cache.computeIfAbsent(type, t -> new HashSet<>()).add(key)) {
                anyNew = true;
            }
        }

        return anyNew;
    }

    public static void invalidate(UUID playerId) {
        playerCache.remove(playerId);
    }

    public static void invalidateImplementedCache() {
        implementedCountCache = null;
    }

    private static Map<CharmType, Set<String>> getOrBuildPlayerCache(ServerPlayer player) {
        return playerCache.computeIfAbsent(player.getUUID(), uuid -> buildFromPokedex(player));
    }

    /**
     * Rebuilds the player's caught-species map from the Pokédex.
     * Only credits forms where {@code FormDexRecord.knowledge == CAUGHT}, so catching
     * a normal Rattata doesn't credit Alolan Rattata's Dark type.
     */
    private static Map<CharmType, Set<String>> buildFromPokedex(ServerPlayer player) {
        Map<CharmType, Set<String>> result = new EnumMap<>(CharmType.class);

        AbstractPokedexManager pokedexData =
                Cobblemon.INSTANCE.getPlayerDataManager().getPokedexData(player.getUUID());

        for (Map.Entry<ResourceLocation, SpeciesDexRecord> entry : pokedexData.getSpeciesRecords().entrySet()) {
            ResourceLocation speciesId = entry.getKey();
            SpeciesDexRecord speciesRecord = entry.getValue();

            Species species = PokemonSpecies.getByIdentifier(speciesId);
            if (species == null || !species.getImplemented()) continue;

            String speciesPath = speciesId.getPath();

            checkAndCreditForm(result, speciesRecord, species.getStandardForm(), speciesPath);

            for (FormData form : species.getForms()) {
                if (form.equals(species.getStandardForm())) continue;
                checkAndCreditForm(result, speciesRecord, form, speciesPath);
            }
        }

        return result;
    }

    private static void checkAndCreditForm(
            Map<CharmType, Set<String>> result,
            SpeciesDexRecord speciesRecord,
            FormData form,
            String speciesPath
    ) {
        FormDexRecord formRecord = speciesRecord.getFormRecord(form.getName());
        if (formRecord == null || formRecord.getKnowledge() != PokedexEntryProgress.OWNED) return;

        String key = speciesPath + ":" + form.getName();
        for (CharmType type : getTypesFromForm(form)) {
            result.computeIfAbsent(type, t -> new HashSet<>()).add(key);
        }
    }

    private static Map<CharmType, Integer> getOrBuildImplementedCache() {
        if (implementedCountCache != null) return implementedCountCache;

        Map<CharmType, Set<String>> tempSets = new EnumMap<>(CharmType.class);

        for (Species species : PokemonSpecies.getSpecies()) {
            if (!species.getImplemented()) continue;

            String speciesPath = species.resourceIdentifier.getPath();

            addFormToTypes(tempSets, species.getStandardForm(), speciesPath);

            for (FormData form : species.getForms()) {
                if (form.equals(species.getStandardForm())) continue;
                addFormToTypes(tempSets, form, speciesPath);
            }
        }

        Map<CharmType, Integer> counts = new EnumMap<>(CharmType.class);
        tempSets.forEach((type, set) -> counts.put(type, set.size()));
        implementedCountCache = counts;
        return implementedCountCache;
    }

    private static void addFormToTypes(Map<CharmType, Set<String>> map, FormData form, String speciesPath) {
        String key = speciesPath + ":" + form.getName();
        for (CharmType type : getTypesFromForm(form)) {
            map.computeIfAbsent(type, t -> new HashSet<>()).add(key);
        }
    }

    private static List<CharmType> getTypesFromForm(FormData form) {
        List<CharmType> types = new ArrayList<>();

        CharmType primary = CharmType.fromElementalType(form.getPrimaryType());
        if (primary != null) types.add(primary);

        ElementalType secondary = form.getSecondaryType();
        if (secondary != null) {
            CharmType ct = CharmType.fromElementalType(secondary);
            if (ct != null) types.add(ct);
        }

        return types;
    }
}