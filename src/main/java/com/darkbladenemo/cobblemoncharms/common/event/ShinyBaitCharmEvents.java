package com.darkbladenemo.cobblemoncharms.common.event;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.fishing.SpawnBait;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.darkbladenemo.cobblemoncharms.common.config.Config;
import com.darkbladenemo.cobblemoncharms.common.util.CharmMultiplierUtils;
import kotlin.Unit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Overrides Cobblemon's own SHINY_REROLL bait effect (shared by fishing bait and Poké Snacks,
 * both routed through SpawnBaitInfluence -> SpawnBait.Effects) so the Shiny Charm also boosts
 * this independent reroll, not just the base SHINY_CHANCE_CALCULATION roll.
 * See ShinyCharmEvents (wild spawns) and ShinyCharmInfluence (honey log) for the other paths.
 */
public class ShinyBaitCharmEvents {

    private static final ResourceLocation SHINY_REROLL =
            ResourceLocation.fromNamespaceAndPath("cobblemon", "shiny_reroll");

    public static void register() {
        CobblemonEvents.BAIT_EFFECT_REGISTRATION.subscribe(Priority.NORMAL, event -> {
            event.registerFunction(SHINY_REROLL, (entity, effect) -> {
                shinyRerollWithCharm(entity, effect);
                return Unit.INSTANCE;
            });
            return Unit.INSTANCE;
        });
    }

    private static void shinyRerollWithCharm(PokemonEntity pokemonEntity, SpawnBait.Effect effect) {
        if (!Config.SHINY_CHARM_AFFECTS_BAIT_AND_SNACKS.get()) return;
        if (pokemonEntity.getPokemon().getShiny()) return;

        int shinyOdds = (int) Cobblemon.INSTANCE.getConfig().getShinyRate();
        if (shinyOdds <= 0) return;

        ServerPlayer player = resolveNearestPlayer(pokemonEntity);
        float multiplier = (player != null) ? CharmMultiplierUtils.getShinyMultiplier(player) : 1.0f;

        double boostedValue = effect.getValue() * multiplier;
        int randomNumber = ThreadLocalRandom.current().nextInt(0, shinyOdds + 1);

        if (randomNumber <= boostedValue) {
            pokemonEntity.getPokemon().setShiny(true);
        }
    }

    @SuppressWarnings("resource")
    private static ServerPlayer resolveNearestPlayer(PokemonEntity pokemonEntity) {
        if (!(pokemonEntity.level() instanceof ServerLevel level)) return null;
        return (ServerPlayer) level.getNearestPlayer(pokemonEntity, Config.TYPE_CHARM_RADIUS.get());
    }
}