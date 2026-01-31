package com.darkbladenemo.cobblemonextraitems.common.influence;

import com.cobblemon.mod.common.api.spawning.SpawnBucket;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.spawning.detail.SpawnAction;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence;
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition;
import com.cobblemon.mod.common.api.spawning.position.calculators.SpawnablePositionCalculator;
import com.darkbladenemo.cobblemonextraitems.common.component.MultiCharmData;
import com.darkbladenemo.cobblemonextraitems.common.component.TypeCharmData;
import com.darkbladenemo.cobblemonextraitems.common.config.Config;
import com.darkbladenemo.cobblemonextraitems.common.item.charm.CharmType;
import com.darkbladenemo.cobblemonextraitems.common.item.charm.TypeCharm;
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents;
import com.darkbladenemo.cobblemonextraitems.init.ModItems;
import com.darkbladenemo.cobblemonextraitems.common.util.CobblemonExtraItemsUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.*;

public class TypeCharmInfluence implements SpawningInfluence {

    private final ServerPlayer player;
    private Map<CharmType, List<CharmInstance>> cachedCharms;
    private int lastCacheTick = 0;
    private static final int CACHE_DURATION_TICKS = 20;

    public TypeCharmInfluence(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public float affectWeight(SpawnDetail detail, SpawnablePosition spawnablePosition, float weight) {
        if (!(detail instanceof PokemonSpawnDetail pokemonDetail)) {
            return weight;
        }

        Map<CharmType, List<CharmInstance>> charmsByType = getCachedPlayerTypeCharms();
        if (charmsByType.isEmpty()) {
            return weight;
        }

        com.cobblemon.mod.common.pokemon.Species species =
                CobblemonExtraItemsUtils.resolveSpecies(pokemonDetail);
        if (species == null) return weight;

        // Calculate positions once
        BlockPos playerPos = player.blockPosition();
        BlockPos spawnPos = spawnablePosition.getPosition();
        double distanceSq = playerPos.distSqr(spawnPos);

        // Accumulate total bonus from all matching types
        // Use array wrapper to allow modification inside lambda
        final float[] totalBonus = {0.0f};

        CobblemonExtraItemsUtils.forEachType(species, elementalType -> {
            CharmType charmType = CharmType.fromElementalType(elementalType);
            if (charmType == null) return;

            List<CharmInstance> charms = charmsByType.get(charmType);
            if (charms == null) return;

            // Sum bonuses from all charms in range
            for (CharmInstance charm : charms) {
                if (distanceSq <= charm.radiusSquared) {
                    totalBonus[0] += (charm.multiplier - 1.0f);
                }
            }
        });

        // Apply total bonus if any
        if (totalBonus[0] > 0) {
            return weight * (1.0f + totalBonus[0]);
        }

        return weight;
    }

    private Map<CharmType, List<CharmInstance>> getCachedPlayerTypeCharms() {
        int currentTick = player.tickCount;
        if (cachedCharms == null || currentTick - lastCacheTick >= CACHE_DURATION_TICKS) {
            cachedCharms = calculatePlayerTypeCharms();
            lastCacheTick = currentTick;
        }
        return cachedCharms;
    }

    private Map<CharmType, List<CharmInstance>> calculatePlayerTypeCharms() {
        // Use EnumMap for better performance with enum keys
        Map<CharmType, List<CharmInstance>> charmsByType = new EnumMap<>(CharmType.class);

        CuriosApi.getCuriosInventory(player).ifPresent(inventory -> {
            inventory.findCurios("type_charm_slot").forEach(slotResult -> {
                ItemStack stack = slotResult.stack();

                if (stack.isEmpty()) {
                    return;
                }

                // Handle regular type charms
                if (stack.getItem() instanceof TypeCharm) {
                    TypeCharmData data = stack.get(ModDataComponents.TYPE_CHARM_DATA.get());

                    // Handle case where component data might be missing (e.g., /give command)
                    if (data != null) {
                        CharmType type = data.type();
                        float multiplier = data.multiplier();
                        double radius = data.radius();

                        addCharmInstance(charmsByType, type, multiplier, radius);
                    } else {
                        // Fallback: Try to determine type from registered items
                        ModItems.TYPE_CHARMS.forEach((type, deferredCharm) -> {
                            if (stack.is(deferredCharm.get())) {
                                float multiplier = Config.TYPE_CHARM_MULTIPLIER.get().floatValue();
                                double radius = Config.TYPE_CHARM_RADIUS.get();

                                addCharmInstance(charmsByType, type, multiplier, radius);
                            }
                        });
                    }
                }
                // Handle multi-charms
                else if (stack.is(ModItems.MULTI_CHARM.get())) {
                    MultiCharmData multiData = stack.get(ModDataComponents.MULTI_CHARM_DATA.get());

                    if (multiData != null) {
                        multiData.getEnabledEffects().forEach((type, effect) -> {
                            addCharmInstance(charmsByType, type, effect.multiplier(), effect.radius());
                        });
                    }
                }
            });
        });

        return charmsByType;
    }

    /**
     * Helper method to add a charm instance to the map.
     * Each charm is tracked separately with its own radius.
     */
    private void addCharmInstance(Map<CharmType, List<CharmInstance>> charmsByType,
                                  CharmType type, float multiplier, double radius) {
        charmsByType.computeIfAbsent(type, k -> new ArrayList<>(4))
                .add(CharmInstance.fromRadius(multiplier, radius));
    }

    /**
     * Represents a single charm instance with pre-calculated squared radius.
     * Stores radius squared to avoid repeated calculation during spawn checks.
     */
    private record CharmInstance(float multiplier, double radiusSquared) {
        /**
         * Creates a CharmInstance from a radius, pre-calculating the squared value.
         */
        static CharmInstance fromRadius(float multiplier, double radius) {
            return new CharmInstance(multiplier, radius * radius);
        }
    }

    @Override
    public boolean isExpired() {
        return false;
    }

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