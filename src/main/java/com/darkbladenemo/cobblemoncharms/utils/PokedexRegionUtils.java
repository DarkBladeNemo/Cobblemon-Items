package com.darkbladenemo.cobblemoncharms.utils;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.pokedex.AbstractPokedexManager;
import com.cobblemon.mod.common.api.pokedex.Dexes;
import com.cobblemon.mod.common.api.pokedex.PokedexEntryProgress;
import com.cobblemon.mod.common.api.pokedex.def.PokedexDef;
import com.cobblemon.mod.common.api.pokedex.entry.PokedexEntry;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import kotlin.Unit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PokedexRegionUtils {

    private static final Map<UUID, Map<String, RegionProgress>> CACHE = new HashMap<>();

    public record RegionProgress(String region, int totalImplemented, int totalSeen, int totalCaught,
                                 double completionPercentage, boolean isCompleted) {
        public RegionProgress(String region, int totalImplemented, int totalSeen, int totalCaught) {
            this(region, totalImplemented, totalSeen, totalCaught,
                    totalImplemented > 0 ? (double) totalCaught / (double) totalImplemented * 100.0 : 0.0,
                    totalImplemented > 0 && totalCaught == totalImplemented);
        }

        @Override
        public @NotNull String toString() {
            return String.format("%s: %d/%d captured (%.1f%%) - %s",
                    region, totalCaught, totalImplemented, completionPercentage,
                    isCompleted ? "COMPLETED" : "IN PROGRESS");
        }
    }

    /** Registers the cache-invalidation listeners. Call during mod construction. */
    public static void register() {
        CobblemonEvents.POKEDEX_DATA_CHANGED_POST.subscribe(Priority.NORMAL, event -> {
            invalidate(event.getPlayerUUID());
            return Unit.INSTANCE;
        });

        NeoForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedOutEvent e) ->
                invalidate(e.getEntity().getUUID())
        );
    }

    public static void invalidate(UUID playerId) {
        CACHE.remove(playerId);
    }

    /**
     * Checks if a player has reached the given completion threshold (0-100) for a region.
     * Used for awarding the Shiny Charm at a configurable dex percentage.
     */
    public static boolean hasReachedThreshold(ServerPlayer player, String region, double threshold) {
        RegionProgress progress = getRegionProgress(player, region);
        if (progress == null) return false;
        return progress.completionPercentage() >= threshold;
    }

    public static boolean isRegionCompleted(ServerPlayer player, String region) {
        return hasReachedThreshold(player, region, 100.0);
    }

    public static RegionProgress getRegionProgress(ServerPlayer player, String region) {
        Map<String, RegionProgress> playerCache =
                CACHE.computeIfAbsent(player.getUUID(), uuid -> new HashMap<>());
        return playerCache.computeIfAbsent(region, r -> computeRegionProgress(player, r));
    }

    private static RegionProgress computeRegionProgress(ServerPlayer player, String region) {
        ResourceLocation dexId = ResourceLocation.fromNamespaceAndPath("cobblemon", region);
        PokedexDef dexDef = Dexes.INSTANCE.getDexEntryMap().get(dexId);
        if (dexDef == null) return null;

        AbstractPokedexManager pokedexData = Cobblemon.INSTANCE
                .getPlayerDataManager().getPokedexData(player.getUUID());

        List<PokedexEntry> entries = dexDef.getEntries();
        int implemented = 0;
        int caught = 0;
        int seen = 0;

        for (PokedexEntry entry : entries) {
            Species species = PokemonSpecies.getByIdentifier(entry.getSpeciesId());
            if (species == null || !species.getImplemented()) continue;

            implemented++;
            PokedexEntryProgress progress = pokedexData.getHighestKnowledgeFor(entry);
            if (progress == PokedexEntryProgress.OWNED) caught++;
            else if (progress == PokedexEntryProgress.SEEN) seen++;
        }

        return new RegionProgress(region, implemented, seen, caught);
    }
}