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

    // Cache for equipped charms - updated every 20 ticks (1 second)
    private Map<CharmType, Float> cachedCharmMultipliers;
    private int lastCharmCacheTick = 0;
    private static final int CHARM_CACHE_DURATION_TICKS = 20;

    // Cache for player position - updated every tick
    private BlockPos cachedPlayerPos;
    private int lastPositionCacheTick = -1;

    // Global values from config - calculated once at class load
    private static final double GLOBAL_RADIUS = Config.TYPE_CHARM_RADIUS.get();
    private static final double GLOBAL_RADIUS_SQUARED = GLOBAL_RADIUS * GLOBAL_RADIUS;
    private static final float GLOBAL_NON_MATCH_PENALTY = Config.TYPE_CHARM_NON_MATCH_MULTIPLIER.get().floatValue();

    public TypeCharmInfluence(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public float affectWeight(SpawnDetail detail, SpawnablePosition spawnablePosition, float weight) {
        if (!(detail instanceof PokemonSpawnDetail pokemonDetail)) {
            return weight;
        }

        // Get cached charm multipliers - updates every second
        Map<CharmType, Float> charmMultipliers = getCachedCharmMultipliers();

        // Early exit: no charms equipped
        if (charmMultipliers.isEmpty()) {
            return weight;
        }

        // Get cached player position - updates every tick
        BlockPos playerPos = getCachedPlayerPosition();
        BlockPos spawnPos = spawnablePosition.getPosition();

        // Early exit: spawn position too far from player
        // Quick Manhattan distance check before expensive squared distance
        double dx = Math.abs(playerPos.getX() - spawnPos.getX());
        double dz = Math.abs(playerPos.getZ() - spawnPos.getZ());

        // If either X or Z distance alone exceeds global radius, can't possibly be in range
        if (dx > GLOBAL_RADIUS || dz > GLOBAL_RADIUS) {
            return weight;
        }

        // Now calculate actual distance squared
        double distanceSq = playerPos.distSqr(spawnPos);

        // Early exit: spawn too far from player
        if (distanceSq > GLOBAL_RADIUS_SQUARED) {
            return weight;
        }

        // Resolve species once
        com.cobblemon.mod.common.pokemon.Species species =
                CobblemonExtraItemsUtils.resolveSpecies(pokemonDetail);
        if (species == null) {
            return weight;
        }

        // Check if this spawn matches any equipped charm types
        final float[] totalMatchBonus = {0.0f};
        final boolean[] matchesAnyCharm = {false};

        CobblemonExtraItemsUtils.forEachType(species, elementalType -> {
            CharmType charmType = CharmType.fromElementalType(elementalType);
            if (charmType == null) return;

            Float multiplier = charmMultipliers.get(charmType);
            if (multiplier != null) {
                matchesAnyCharm[0] = true;
                // Accumulate match bonuses additively
                totalMatchBonus[0] += (multiplier - 1.0f);
            }
        });

        // Apply effects based on whether spawn matches
        if (matchesAnyCharm[0]) {
            // This spawn matches at least one charm - apply match bonus
            if (totalMatchBonus[0] > 0) {
                weight *= (1.0f + totalMatchBonus[0]);
            }
        } else {
            // This spawn doesn't match any charm - apply global penalty
            weight *= GLOBAL_NON_MATCH_PENALTY;
        }

        return weight;
    }

    /**
     * Gets cached player position, updating cache every tick.
     */
    private BlockPos getCachedPlayerPosition() {
        int currentTick = player.tickCount;
        if (cachedPlayerPos == null || currentTick != lastPositionCacheTick) {
            cachedPlayerPos = player.blockPosition();
            lastPositionCacheTick = currentTick;
        }
        return cachedPlayerPos;
    }

    /**
     * Gets cached charm multipliers, updating every 20 ticks (1 second).
     */
    private Map<CharmType, Float> getCachedCharmMultipliers() {
        int currentTick = player.tickCount;
        if (cachedCharmMultipliers == null || currentTick - lastCharmCacheTick >= CHARM_CACHE_DURATION_TICKS) {
            cachedCharmMultipliers = calculateCharmMultipliers();
            lastCharmCacheTick = currentTick;
        }
        return cachedCharmMultipliers;
    }

    /**
     * Calculates aggregated match multipliers for each charm type.
     * Non-match penalty is global from config.
     */
    private Map<CharmType, Float> calculateCharmMultipliers() {
        // Use EnumMap for better performance with enum keys
        Map<CharmType, Float> multipliers = new EnumMap<>(CharmType.class);

        CuriosApi.getCuriosInventory(player).ifPresent(inventory ->
                inventory.findCurios("type_charm_slot").forEach(slotResult -> {
            ItemStack stack = slotResult.stack();

            if (stack.isEmpty()) {
                return;
            }

            // Handle regular type charms
            if (stack.getItem() instanceof TypeCharm) {
                TypeCharmData data = stack.get(ModDataComponents.TYPE_CHARM_DATA.get());

                if (data != null) {
                    CharmType type = data.type();
                    float multiplier = data.matchMultiplier();
                    addMultiplier(multipliers, type, multiplier);
                } else {
                    // Fallback: Try to determine type from registered items
                    ModItems.TYPE_CHARMS.forEach((type, deferredCharm) -> {
                        if (stack.is(deferredCharm.get())) {
                            float multiplier = Config.TYPE_CHARM_MATCH_MULTIPLIER.get().floatValue();
                            addMultiplier(multipliers, type, multiplier);
                        }
                    });
                }
            }
            // Handle multi-charms
            else if (stack.is(ModItems.MULTI_CHARM.get())) {
                MultiCharmData multiData = stack.get(ModDataComponents.MULTI_CHARM_DATA.get());

                if (multiData != null) {
                    multiData.getEnabledEffects().forEach((type, effect) ->
                            addMultiplier(multipliers, type, effect.matchMultiplier()));
                }
            }
        }));

        return multipliers;
    }

    /**
     * Adds a charm's multiplier to the aggregated total for a type.
     * Multiple charms of the same type stack additively.
     */
    private void addMultiplier(Map<CharmType, Float> multipliers, CharmType type, float multiplier) {
        multipliers.merge(type, multiplier, (existing, newVal) -> {
            // Additive stacking: combine bonuses
            // e.g., 5.0 + 5.0 = 1.0 + (4.0 + 4.0) = 9.0
            return 1.0f + ((existing - 1.0f) + (newVal - 1.0f));
        });
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