package com.darkbladenemo.cobblemoncharms.common.influence;

import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.spawning.detail.SpawnAction;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence;
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition;
import com.cobblemon.mod.common.api.spawning.position.calculators.SpawnablePositionCalculator;
import com.darkbladenemo.cobblemoncharms.advancement.ModAdvancement;
import com.darkbladenemo.cobblemoncharms.common.component.MultiCharmData;
import com.darkbladenemo.cobblemoncharms.common.component.TypeCharmData;
import com.darkbladenemo.cobblemoncharms.common.config.Config;
import com.darkbladenemo.cobblemoncharms.common.item.charm.CharmType;
import com.darkbladenemo.cobblemoncharms.common.item.charm.TypeCharm;
import com.darkbladenemo.cobblemoncharms.common.util.cobblemoncharmsUtils;
import com.darkbladenemo.cobblemoncharms.init.ModDataComponents;
import com.darkbladenemo.cobblemoncharms.init.ModItems;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TypeCharmInfluence implements SpawningInfluence {

    private final ServerPlayer player;

    private Map<CharmType, Float> cachedCharmMultipliers;
    private int lastCharmCacheTick = 0;
    private static final int CHARM_CACHE_DURATION_TICKS = 20;

    private double cachedRadius;
    private double cachedRadiusSq;
    private float cachedNonMatchPenalty;

    private BlockPos cachedPlayerPos;
    private int lastPositionCacheTick = -1;

    public TypeCharmInfluence(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public float affectWeight(@NotNull SpawnDetail detail,
                              @NotNull SpawnablePosition spawnablePosition,
                              float weight) {
        if (!(detail instanceof PokemonSpawnDetail pokemonDetail)) return weight;

        Map<CharmType, Float> charmMultipliers = getCachedCharmMultipliers();
        if (charmMultipliers.isEmpty()) return weight;

        BlockPos playerPos = getCachedPlayerPosition();
        BlockPos spawnPos  = spawnablePosition.getPosition();

        double dx = Math.abs(playerPos.getX() - spawnPos.getX());
        double dz = Math.abs(playerPos.getZ() - spawnPos.getZ());
        if (dx > cachedRadius || dz > cachedRadius) return weight;
        if (playerPos.distSqr(spawnPos) > cachedRadiusSq) return weight;

        com.cobblemon.mod.common.pokemon.Species species =
                cobblemoncharmsUtils.resolveSpecies(pokemonDetail);
        if (species == null) return weight;

        final float[] totalMatchBonus   = {0.0f};
        final boolean[] matchesAnyCharm = {false};

        cobblemoncharmsUtils.forEachType(species, elementalType -> {
            CharmType charmType = CharmType.fromElementalType(elementalType);
            if (charmType == null) return;
            Float multiplier = charmMultipliers.get(charmType);
            if (multiplier != null) {
                matchesAnyCharm[0] = true;
                totalMatchBonus[0] += (multiplier - 1.0f);
            }
        });

        if (matchesAnyCharm[0]) {
            if (totalMatchBonus[0] > 0) weight *= (1.0f + totalMatchBonus[0]);
        } else {
            weight *= cachedNonMatchPenalty;
        }

        return weight;
    }

    private BlockPos getCachedPlayerPosition() {
        int currentTick = player.tickCount;
        if (cachedPlayerPos == null || currentTick != lastPositionCacheTick) {
            cachedPlayerPos = player.blockPosition();
            lastPositionCacheTick = currentTick;
        }
        return cachedPlayerPos;
    }

    private Map<CharmType, Float> getCachedCharmMultipliers() {
        int currentTick = player.tickCount;
        if (cachedCharmMultipliers == null
                || currentTick - lastCharmCacheTick >= CHARM_CACHE_DURATION_TICKS) {
            cachedCharmMultipliers = calculateCharmMultipliers();

            cachedRadius          = Config.TYPE_CHARM_RADIUS.get();
            cachedRadiusSq        = cachedRadius * cachedRadius;
            cachedNonMatchPenalty = (float) Config.TYPE_CHARM_NON_MATCH_MULTIPLIER.get();

            lastCharmCacheTick = currentTick;
        }
        return cachedCharmMultipliers;
    }

    private Map<CharmType, Float> calculateCharmMultipliers() {
        Map<CharmType, Float> multipliers = new EnumMap<>(CharmType.class);

        var optional = TrinketsApi.getTrinketComponent(player);
        if (optional.isEmpty()) return multipliers;

        for (Map<String, TrinketInventory> slotGroup : optional.get().getInventory().values()) {
            for (TrinketInventory inv : slotGroup.values()) {
                for (int i = 0; i < inv.getContainerSize(); i++) {
                    ItemStack stack = inv.getItem(i);
                    if (!stack.isEmpty()) processStack(stack, multipliers);
                }
            }
        }

        return multipliers;
    }

    private void processStack(ItemStack stack, Map<CharmType, Float> multipliers) {
        if (stack.getItem() instanceof TypeCharm) {
            TypeCharmData data = stack.get(ModDataComponents.TYPE_CHARM_DATA);
            if (data == null) {
                ModItems.TYPE_CHARMS.forEach((type, charm) -> {
                    if (stack.is(charm) && isTypeEffectAllowed(type)) {
                        addMultiplier(multipliers, type,
                                (float) Config.TYPE_CHARM_MATCH_MULTIPLIER.get());
                    }
                });
            } else {
                if (isTypeEffectAllowed(data.type())) {
                    addMultiplier(multipliers, data.type(), data.matchMultiplier());
                }
            }
        } else if (stack.is(ModItems.MULTI_CHARM)) {
            MultiCharmData multiData = stack.get(ModDataComponents.MULTI_CHARM_DATA);
            if (multiData != null) {
                multiData.getEnabledEffects().forEach((type, effect) -> {
                    if (isTypeEffectAllowed(type)) {
                        addMultiplier(multipliers, type, effect.matchMultiplier());
                    }
                });
            }
        }
    }

    private void addMultiplier(Map<CharmType, Float> multipliers, CharmType type, float multiplier) {
        multipliers.merge(type, multiplier, (existing, newVal) ->
                1.0f + ((existing - 1.0f) + (newVal - 1.0f))
        );
    }

    private boolean isTypeEffectAllowed(CharmType type) {
        if (!Config.isTypeCharmEnabled(type)) return false;
        if (!Config.CHARM_EFFECT_REQUIRES_ADVANCEMENT.get()) return true;
        @SuppressWarnings("resource")
        ServerLevel level = player.serverLevel();
        var advancement = ModAdvancement.getTypeCharmAdvancement(level.getServer(), type);
        if (advancement == null) return true;
        return player.getAdvancements().getOrStartProgress(advancement).isDone();
    }

    public boolean isExpired() {
        return player.isRemoved();
    }

    @Override public void affectAction(@NotNull SpawnAction<?> action) {}
    @Override public void affectSpawn(@NotNull SpawnAction<?> action, @NotNull Entity entity) {}

    @Override
    public boolean isAllowedPosition(@NotNull ServerLevel world, @NotNull BlockPos pos,
                                     @NotNull SpawnablePositionCalculator<?, ?> calculator) {
        return true;
    }

    @Override
    public boolean affectSpawnable(@NotNull SpawnDetail detail,
                                   @NotNull SpawnablePosition position) {
        return true;
    }
}