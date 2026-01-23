package com.darkbladenemo.cobblemonextraitems.influence;

import com.cobblemon.mod.common.api.spawning.SpawnBucket;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.spawning.detail.SpawnAction;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence;
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition;
import com.cobblemon.mod.common.api.spawning.position.calculators.SpawnablePositionCalculator;
import com.darkbladenemo.cobblemonextraitems.config.Config;
import com.darkbladenemo.cobblemonextraitems.init.ModItems;
import com.darkbladenemo.cobblemonextraitems.item.charm.CharmType;
import com.darkbladenemo.cobblemonextraitems.item.charm.TypeCharm;
import com.darkbladenemo.cobblemonextraitems.util.CobblemonExtraItemsUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.HashMap;
import java.util.Map;

public class TypeCharmInfluence implements SpawningInfluence {

    private final ServerPlayer player;
    // Add caching for performance
    private Map<String, Integer> cachedCharms;
    private long lastCacheTime = 0;

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

        // Get player's equipped type charms (with caching)
        Map<String, Integer> typeBoosts = getCachedPlayerTypeCharms();
        if (typeBoosts.isEmpty()) {
            return weight;
        }

        // Get Pokémon species using utility method
        com.cobblemon.mod.common.pokemon.Species species =
                CobblemonExtraItemsUtils.resolveSpecies(pokemonDetail);
        if (species == null) return weight;

        float newWeight = weight;

        // Apply boosts for each type the player has charms for
        for (Map.Entry<String, Integer> entry : typeBoosts.entrySet()) {
            String charmTypeName = entry.getKey();
            int boostLevel = entry.getValue();

            // Check if this Pokémon has the type using the utility method
            if (CobblemonExtraItemsUtils.speciesHasType(species, charmTypeName)) {
                // Apply multiplier based on config and boost level
                float baseMultiplier = Config.TYPE_CHARM_MULTIPLIER.get().floatValue();
                float totalMultiplier = 1.0f + ((baseMultiplier - 1.0f) * boostLevel);
                newWeight *= totalMultiplier;
            }
        }

        return newWeight;
    }

    private Map<String, Integer> getCachedPlayerTypeCharms() {
        // Cache for 1 second to reduce Curios API calls
        long currentTime = System.currentTimeMillis();
        if (cachedCharms == null || currentTime - lastCacheTime > 1000) {
            cachedCharms = calculatePlayerTypeCharms();
            lastCacheTime = currentTime;
        }
        return cachedCharms;
    }

    private Map<String, Integer> calculatePlayerTypeCharms() {
        Map<String, Integer> typeBoosts = new HashMap<>();

        CuriosApi.getCuriosInventory(player).ifPresent(inventory -> {
            // Only check the dedicated type charm slot
            for (SlotResult slotResult : inventory.findCurios("type_charm_slot")) {
                ItemStack stack = slotResult.stack();

                // Loop through all type charms in ModItems
                for (Map.Entry<CharmType, DeferredHolder<Item, TypeCharm>> entry :
                        ModItems.TYPE_CHARMS.entrySet()) {
                    CharmType type = entry.getKey();
                    TypeCharm charmItem = entry.getValue().get();

                    if (stack.is(charmItem)) {
                        typeBoosts.merge(type.getTranslationKey(), 1, Integer::sum);
                    }
                }
            }
        });

        return typeBoosts;
    }

    // Other required methods remain the same...
    @Override
    public boolean isExpired() { return false; }
    @Override
    public void affectAction(SpawnAction<?> action) { }
    @Override
    public void affectSpawn(SpawnAction<?> action, Entity entity) { }
    @Override
    public void affectBucketWeights(Map<SpawnBucket, Float> bucketWeights) { }
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