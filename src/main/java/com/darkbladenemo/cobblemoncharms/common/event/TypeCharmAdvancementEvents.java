package com.darkbladenemo.cobblemoncharms.common.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.PokedexDataChangedEvent;
import com.cobblemon.mod.common.api.pokedex.FormDexRecord;
import com.cobblemon.mod.common.api.pokedex.PokedexEntryProgress;
import com.cobblemon.mod.common.pokemon.FormData;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.darkbladenemo.cobblemoncharms.advancement.ModAdvancement;
import com.darkbladenemo.cobblemoncharms.common.config.Config;
import com.darkbladenemo.cobblemoncharms.common.item.charm.CharmType;
import com.darkbladenemo.cobblemoncharms.common.tracking.TypeCharmProgressTracker;
import com.darkbladenemo.cobblemoncharms.init.ModItems;
import com.darkbladenemo.cobblemoncharms.utils.AdvancementUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import kotlin.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Listens for Pokédex catches and grants the appropriate type charm advancement and item
 * reward when a player reaches the configured catch threshold for a given type.
 * Also invalidates the per-player progress cache on logout.
 */
public class TypeCharmAdvancementEvents {

    private static MinecraftServer currentServer = null;

    public static void register() {
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTED.register(
                server -> currentServer = server
        );
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPED.register(
                server -> currentServer = null
        );

        CobblemonEvents.POKEDEX_DATA_CHANGED_POST.subscribe(Priority.NORMAL, event -> {
            handlePokedexChanged(event);
            return Unit.INSTANCE;
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                checkAllTypeCharmsForPlayer(handler.player)
        );

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            if (handler.player != null) {
                TypeCharmProgressTracker.invalidate(handler.player.getUUID());
            }
        });
    }

    private static void checkAllTypeCharmsForPlayer(ServerPlayer player) {
        if (!Config.ENABLE_ALL_TYPE_CHARMS.get()) return;
        if (currentServer == null) return;

        double threshold = Config.TYPE_CHARM_THRESHOLD_PERCENTAGE.get();

        for (CharmType type : CharmType.getEntries()) {
            if (!Config.isTypeCharmEnabled(type)) continue;

            int required = TypeCharmProgressTracker.computeThreshold(type, threshold);
            int current  = TypeCharmProgressTracker.getUniqueCount(player, type);
            if (current < required) continue;

            AdvancementHolder advancement = ModAdvancement.getTypeCharmAdvancement(currentServer, type);
            if (advancement == null) continue;

            boolean granted = AdvancementUtils.grantAdvancement(player, advancement);
            if (!granted) continue;

            if (!Config.GRANT_CHARM_ON_ADVANCEMENT.get()) continue;

            ItemStack charm = new ItemStack(ModItems.TYPE_CHARMS.get(type));
            if (!player.getInventory().add(charm)) player.drop(charm, false);

            player.sendSystemMessage(Component.translatable(
                    "message.cobblemoncharms.type_charm_awarded",
                    Component.translatable("cobblemon.type." + type.getTranslationKey())
            ));
        }
    }

    private static void handlePokedexChanged(PokedexDataChangedEvent.Post event) {
        if (event.getKnowledge() != PokedexEntryProgress.CAUGHT) return;
        if (currentServer == null) return;

        ServerPlayer player = currentServer.getPlayerList().getPlayer(event.getPlayerUUID());
        if (player == null) return;

        if (!Config.ENABLE_ALL_TYPE_CHARMS.get()) return;

        Pokemon pokemon = event.getDataSource().getPokemon();
        FormDexRecord formRecord = event.getRecord();

        boolean anyNew = TypeCharmProgressTracker.record(player, pokemon, formRecord);
        if (!anyNew) return;

        double threshold = Config.TYPE_CHARM_THRESHOLD_PERCENTAGE.get();

        for (CharmType type : getTypesForForm(pokemon, formRecord)) {
            if (!Config.isTypeCharmEnabled(type)) continue;

            int required = TypeCharmProgressTracker.computeThreshold(type, threshold);
            int current  = TypeCharmProgressTracker.getUniqueCount(player, type);
            if (current < required) continue;

            AdvancementHolder advancement = ModAdvancement.getTypeCharmAdvancement(currentServer, type);
            if (advancement == null) continue;

            boolean granted = AdvancementUtils.grantAdvancement(player, advancement);
            if (!granted) continue;

            if (!Config.GRANT_CHARM_ON_ADVANCEMENT.get()) continue;

            ItemStack charm = new ItemStack(ModItems.TYPE_CHARMS.get(type));
            if (!player.getInventory().add(charm)) player.drop(charm, false);

            player.sendSystemMessage(Component.translatable(
                    "message.cobblemoncharms.type_charm_awarded",
                    Component.translatable("cobblemon.type." + type.getTranslationKey())
            ));
        }
    }

    private static List<CharmType> getTypesForForm(Pokemon pokemon, FormDexRecord formRecord) {
        List<CharmType> types = new ArrayList<>();
        FormData form = pokemon.getSpecies().getFormByName(formRecord.getFormName());

        CharmType primary = CharmType.fromElementalType(form.getPrimaryType());
        if (primary != null) types.add(primary);

        var secondary = form.getSecondaryType();
        if (secondary != null) {
            CharmType ct = CharmType.fromElementalType(secondary);
            if (ct != null) types.add(ct);
        }

        return types;
    }
}