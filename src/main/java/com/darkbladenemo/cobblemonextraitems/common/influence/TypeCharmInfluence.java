package com.darkbladenemo.cobblemonextraitems.common.influence;

import com.cobblemon.mod.common.api.spawning.SpawnBucket;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.spawning.detail.SpawnAction;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence;
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition;
import com.cobblemon.mod.common.api.spawning.position.calculators.SpawnablePositionCalculator;
import com.darkbladenemo.cobblemonextraitems.common.component.MultiCharmData;
import com.darkbladenemo.cobblemonextraitems.common.component.TypeCharmData;  // ADD THIS IMPORT
import com.darkbladenemo.cobblemonextraitems.common.config.Config;
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents;  // ADD THIS IMPORT
import com.darkbladenemo.cobblemonextraitems.init.ModItems;
import com.darkbladenemo.cobblemonextraitems.common.util.CobblemonExtraItemsUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.HashMap;
import java.util.Map;

public class TypeCharmInfluence implements SpawningInfluence {

    private final ServerPlayer player;
    // Change the cache type from Map<String, Integer> to Map<String, CharmInfo>
    private Map<String, CharmInfo> cachedCharms;  // CHANGED TYPE HERE
    private long lastCacheTime = 0;
    private static final long CACHE_DURATION_MS = 1000;

    public TypeCharmInfluence(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public float affectWeight(SpawnDetail detail, SpawnablePosition spawnablePosition, float weight) {
        if (!(detail instanceof PokemonSpawnDetail pokemonDetail)) {
            return weight;
        }

        Map<String, CharmInfo> typeBoosts = getCachedPlayerTypeCharms();
        if (typeBoosts.isEmpty()) {
            return weight;
        }

        BlockPos spawnPos = spawnablePosition.getPosition();
        com.cobblemon.mod.common.pokemon.Species species =
                CobblemonExtraItemsUtils.resolveSpecies(pokemonDetail);
        if (species == null) return weight;

        float newWeight = weight;

        for (Map.Entry<String, CharmInfo> entry : typeBoosts.entrySet()) {
            String charmTypeName = entry.getKey();
            CharmInfo info = entry.getValue();

            // Check radius from charm data
            double distanceSq = player.blockPosition().distSqr(spawnPos);
            if (distanceSq > info.radius * info.radius) {
                continue;
            }

            if (CobblemonExtraItemsUtils.speciesHasType(species, charmTypeName)) {
                float totalMultiplier = 1.0f + ((info.multiplier - 1.0f) * info.count);
                newWeight *= totalMultiplier;
            }
        }

        return newWeight;
    }

    private Map<String, CharmInfo> getCachedPlayerTypeCharms() {  // CHANGED RETURN TYPE HERE
        long currentTime = System.currentTimeMillis();
        if (cachedCharms == null || currentTime - lastCacheTime > CACHE_DURATION_MS) {
            cachedCharms = calculatePlayerTypeCharms();
            lastCacheTime = currentTime;
        }
        return cachedCharms;
    }

    private Map<String, CharmInfo> calculatePlayerTypeCharms() {
        Map<String, CharmInfo> typeBoosts = new HashMap<>();

        CuriosApi.getCuriosInventory(player).ifPresent(inventory -> {
            // Check type charm slot for regular type charms
            inventory.findCurios("type_charm_slot").forEach(slotResult -> {
                ItemStack stack = slotResult.stack();

                // Handle regular type charms
                ModItems.TYPE_CHARMS.forEach((type, deferredCharm) -> {
                    if (stack.is(deferredCharm.get())) {
                        TypeCharmData data = stack.get(ModDataComponents.TYPE_CHARM_DATA.get());

                        float multiplier = data != null
                                ? data.multiplier()
                                : Config.TYPE_CHARM_MULTIPLIER.get().floatValue();

                        double radius = data != null
                                ? data.radius()
                                : Config.TYPE_CHARM_RADIUS.get();

                        String typeName = type.getTranslationKey();
                        typeBoosts.merge(
                                typeName,
                                new CharmInfo(1, multiplier, radius),
                                (existing, newInfo) -> new CharmInfo(
                                        existing.count + 1,
                                        existing.multiplier,
                                        existing.radius
                                )
                        );
                    }
                });

                // Handle multi-charms
                if (stack.is(ModItems.MULTI_CHARM.get())) {
                    MultiCharmData multiData = stack.get(ModDataComponents.MULTI_CHARM_DATA.get());
                    if (multiData != null) {
                        multiData.getEnabledEffects().forEach((type, effect) -> {
                            String typeName = type.getTranslationKey();
                            typeBoosts.merge(
                                    typeName,
                                    new CharmInfo(1, effect.multiplier(), effect.radius()),
                                    (existing, newInfo) -> new CharmInfo(
                                            existing.count + 1,
                                            existing.multiplier,
                                            existing.radius
                                    )
                            );
                        });
                    }
                }
            });
        });

        return typeBoosts;
    }

    private static class CharmInfo {
        final int count;
        final float multiplier;
        final double radius;

        CharmInfo(int count, float multiplier, double radius) {
            this.count = count;
            this.multiplier = multiplier;
            this.radius = radius;
        }
    }

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