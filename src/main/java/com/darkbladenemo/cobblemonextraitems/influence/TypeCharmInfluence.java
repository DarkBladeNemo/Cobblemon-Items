package com.darkbladenemo.cobblemonextraitems.influence;

import com.cobblemon.mod.common.api.spawning.SpawnBucket;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.spawning.detail.SpawnAction;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence;
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition;
import com.cobblemon.mod.common.api.spawning.position.calculators.SpawnablePositionCalculator;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.darkbladenemo.cobblemonextraitems.config.Config;
import com.darkbladenemo.cobblemonextraitems.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.HashMap;
import java.util.Map;

public class TypeCharmInfluence implements SpawningInfluence {

    private final ServerPlayer player;

    public TypeCharmInfluence(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public float affectWeight(SpawnDetail detail, SpawnablePosition spawnablePosition, float weight) {
        // Only affect Pokémon spawns
        if (!(detail instanceof PokemonSpawnDetail pokemonDetail)) {
            return weight;
        }

        // Check distance
        double radius = Config.TYPE_CHARM_RADIUS.get();
        BlockPos spawnPos = spawnablePosition.getPosition();
        double distanceSq = player.blockPosition().distSqr(spawnPos);
        if (distanceSq > radius * radius) {
            return weight;
        }

        // Get player's equipped type charms
        Map<String, Integer> typeBoosts = getPlayerTypeCharms();
        if (typeBoosts.isEmpty()) {
            return weight;
        }

        // Get Pokémon species using CobbleCuisine's pattern
        Species species = resolveSpecies(pokemonDetail);
        if (species == null) return weight;

        ElementalType primary = species.getPrimaryType();
        ElementalType secondary = species.getSecondaryType();

        float newWeight = weight;

        // Apply boosts for each type the player has charms for
        for (Map.Entry<String, Integer> entry : typeBoosts.entrySet()) {
            String charmTypeName = entry.getKey();
            int boostLevel = entry.getValue(); // How many charms of this type

            // Check if this Pokémon has the type
            boolean matches = false;
            if (primary != null && primary.getName().equalsIgnoreCase(charmTypeName)) {
                matches = true;
            } else if (secondary != null && secondary.getName().equalsIgnoreCase(charmTypeName)) {
                matches = true;
            }

            if (matches) {
                // Apply multiplier based on config and boost level
                float baseMultiplier = Config.TYPE_CHARM_MULTIPLIER.get().floatValue();
                float totalMultiplier = 1.0f + ((baseMultiplier - 1.0f) * boostLevel);
                newWeight *= totalMultiplier;
            }
        }

        return newWeight;
    }

    // Copy CobbleCuisine's resolveSpecies method exactly
    private Species resolveSpecies(PokemonSpawnDetail p) {
        String species = p.getPokemon().getSpecies();
        return species == null ? null : PokemonSpecies.INSTANCE.getByIdentifier(
                species.indexOf(':') >= 0 ?
                        ResourceLocation.parse(species) :
                        ResourceLocation.fromNamespaceAndPath("cobblemon", species)
        );
    }

    private Map<String, Integer> getPlayerTypeCharms() {
        Map<String, Integer> typeBoosts = new HashMap<>();

        CuriosApi.getCuriosInventory(player).ifPresent(inventory -> {
            // Check all slots that might contain charms
            String[] charmSlots = {"charm", "ring", "necklace", "type_charm_slot"};

            for (String slot : charmSlots) {
                for (SlotResult slotResult : inventory.findCurios(slot)) {
                    ItemStack stack = slotResult.stack();

                    // Check for each type charm
                    if (stack.is(ModItems.FIRE_CHARM.get())) {
                        typeBoosts.merge("fire", 1, Integer::sum);
                    } else if (stack.is(ModItems.WATER_CHARM.get())) {
                        typeBoosts.merge("water", 1, Integer::sum);
                    } else if (stack.is(ModItems.GRASS_CHARM.get())) {
                        typeBoosts.merge("grass", 1, Integer::sum);
                    }
                    // Add checks for all 18 types...
                }
            }
        });

        return typeBoosts;
    }

    // Other required methods from SpawningInfluence interface
    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public void affectAction(SpawnAction<?> action) {
        // Optional: modify spawn action
    }

    @Override
    public void affectSpawn(SpawnAction<?> action, Entity entity) {
        // Optional: modify spawned entity
    }

    @Override
    public void affectBucketWeights(Map<SpawnBucket, Float> bucketWeights) {
        // Optional: modify bucket weights
    }

    @Override
    public boolean isAllowedPosition(ServerLevel world, BlockPos pos,
                                     SpawnablePositionCalculator<?, ?> calculator) {
        return true;
    }

    @Override
    public boolean affectSpawnable(SpawnDetail detail, SpawnablePosition position) {
        return true;
    }
}