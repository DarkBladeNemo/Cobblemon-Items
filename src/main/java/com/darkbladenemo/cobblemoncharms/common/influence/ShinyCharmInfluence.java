package com.darkbladenemo.cobblemoncharms.common.influence;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.spawning.detail.SpawnAction;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.influence.SaccharineLogSlatheredInfluence;
import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence;
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition;
import com.cobblemon.mod.common.api.spawning.position.calculators.SpawnablePositionCalculator;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.darkbladenemo.cobblemoncharms.common.config.Config;
import com.darkbladenemo.cobblemoncharms.common.util.CharmMultiplierUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Extends the Shiny Charm's boost to spawn sources that don't go through Cobblemon's
 * {@code ShinyChanceCalculationEvent} — currently the honey-slathered saccharine log.
 * <p>
 * Shares its multiplier with the normal wild-spawn shiny boost via
 * {@link CharmMultiplierUtils#getShinyMultiplier(ServerPlayer)}
 * See {@code ShinyCharmEvents} for the wild-spawn side.
 */
public class ShinyCharmInfluence implements SpawningInfluence {

    private final ServerPlayer player;

    public ShinyCharmInfluence(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public void affectSpawn(@NotNull SpawnAction<?> action, @NotNull Entity entity) {
        if (!(entity instanceof PokemonEntity pokemonEntity)) {
            return;
        }

        if (pokemonEntity.getPokemon().getShiny()) {
            return;
        }

        boolean fromHoneyLog = action.getSpawnablePosition().getMarkers()
                .contains(SaccharineLogSlatheredInfluence.SACCHARINE_LOG_SLATHERED_MARKER);

        if (fromHoneyLog && Config.SHINY_CHARM_AFFECTS_HONEY_LOG.get()) {
            rerollHoneyLogShiny(pokemonEntity);
        }
    }

    private void rerollHoneyLogShiny(PokemonEntity pokemonEntity) {
        float multiplier = CharmMultiplierUtils.getShinyMultiplier(player);

        // Only reroll if the charm is actually equipped and active
        if (multiplier <= 1.0f) {
            return;
        }

        int baseChance = Cobblemon.INSTANCE.getConfig().getHoneySlatherShinyChance();
        if (baseChance <= 0) {
            return;
        }

        int boostedChance = Math.max(1, Math.round(baseChance / multiplier));

        if (ThreadLocalRandom.current().nextInt(boostedChance) == 0) {
            pokemonEntity.getPokemon().setShiny(true);
        }
    }

    public boolean isExpired() {
        return player.isRemoved();
    }

    @Override
    public void affectAction(@NotNull SpawnAction<?> action) { }

    @Override
    public boolean isAllowedPosition(@NotNull ServerLevel world, @NotNull BlockPos pos,
                                     @NotNull SpawnablePositionCalculator<?, ?> calculator) {
        return true;
    }

    @Override
    public boolean affectSpawnable(@NotNull SpawnDetail detail, @NotNull SpawnablePosition position) {
        return true;
    }
}