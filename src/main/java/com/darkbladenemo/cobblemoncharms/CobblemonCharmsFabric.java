package com.darkbladenemo.cobblemoncharms;

import com.cobblemon.mod.common.api.spawning.spawner.PlayerSpawnerFactory;
import com.darkbladenemo.cobblemoncharms.common.command.CCharmsCommand;
import com.darkbladenemo.cobblemoncharms.common.config.Config;
import com.darkbladenemo.cobblemoncharms.common.event.*;
import com.darkbladenemo.cobblemoncharms.common.influence.ShinyCharmInfluence;
import com.darkbladenemo.cobblemoncharms.common.influence.TypeCharmInfluence;
import com.darkbladenemo.cobblemoncharms.init.ModDataComponents;
import com.darkbladenemo.cobblemoncharms.init.ModItems;
import com.darkbladenemo.cobblemoncharms.init.ModRecipes;
import com.darkbladenemo.cobblemoncharms.network.ModNetworking;
import com.darkbladenemo.cobblemoncharms.tick.TickManager;
import com.darkbladenemo.cobblemoncharms.utils.PokedexRegionUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;

public class CobblemonCharmsFabric implements ModInitializer {

    public static final String MOD_ID = "cobblemoncharms";

    @Override
    public void onInitialize() {

        // Register Config
        Config.init();

        // Register Components
        ModDataComponents.register();

        // Register Items
        ModItems.register();

        // Register Recipes
        ModRecipes.register();

        // Register Network
        ModNetworking.registerServer();

        // Register Cobblemon/Charm events
        ShinyCharmEvents.register();
        ExpCharmEvents.register();
        CatchCharmEvents.register();
        AdvancementRewardHandler.register();
        TypeCharmAdvancementEvents.register();
        AdvancementSyncEvents.register();
        PokedexRegionUtils.register();

        // Register tick manager
        TickManager.register();

        // Register commands
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                CCharmsCommand.register(dispatcher)
        );

        // Register charm spawning influence
        PlayerSpawnerFactory.INSTANCE.getInfluenceBuilders().add(TypeCharmInfluence::new);
        PlayerSpawnerFactory.INSTANCE.getInfluenceBuilders().add(ShinyCharmInfluence::new);

        // Register creative tab entries
        registerCreativeTabEntries();
    }

    private void registerCreativeTabEntries() {

        final boolean evEnabled = Config.ENABLE_ALL_EV_ITEMS.get();
        final boolean ivEnabled = Config.ENABLE_ALL_IV_ITEMS.get();
        final boolean shinyEnabled = Config.ENABLE_SHINY_CHARM.get();
        final boolean expEnabled = Config.ENABLE_EXP_CHARM.get();
        final boolean catchEnabled = Config.ENABLE_CATCH_CHARM.get();
        final boolean multiEnabled = Config.ENABLE_MULTI_CHARM.get();

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {

            if (evEnabled) {
                if (Config.ENABLE_SUPER_CARBOS.get()) entries.accept(ModItems.SUPER_CARBOS);
                if (Config.ENABLE_SUPER_PROTEIN.get()) entries.accept(ModItems.SUPER_PROTEIN);
                if (Config.ENABLE_SUPER_HP_UP.get()) entries.accept(ModItems.SUPER_HP_UP);
                if (Config.ENABLE_SUPER_IRON.get()) entries.accept(ModItems.SUPER_IRON);
                if (Config.ENABLE_SUPER_CALCIUM.get()) entries.accept(ModItems.SUPER_CALCIUM);
                if (Config.ENABLE_SUPER_ZINC.get()) entries.accept(ModItems.SUPER_ZINC);
            }

            if (ivEnabled) {
                if (Config.ENABLE_SUPER_HEALTH_CANDY.get()) entries.accept(ModItems.SUPER_HEALTH_CANDY);
                if (Config.ENABLE_SUPER_MIGHTY_CANDY.get()) entries.accept(ModItems.SUPER_MIGHTY_CANDY);
                if (Config.ENABLE_SUPER_TOUGH_CANDY.get()) entries.accept(ModItems.SUPER_TOUGH_CANDY);
                if (Config.ENABLE_SUPER_SMART_CANDY.get()) entries.accept(ModItems.SUPER_SMART_CANDY);
                if (Config.ENABLE_SUPER_COURAGE_CANDY.get()) entries.accept(ModItems.SUPER_COURAGE_CANDY);
                if (Config.ENABLE_SUPER_QUICK_CANDY.get()) entries.accept(ModItems.SUPER_QUICK_CANDY);
                if (Config.ENABLE_GOLD_BOTTLE_CAP.get()) entries.accept(ModItems.GOLD_BOTTLE_CAP);
            }

            if (shinyEnabled) entries.accept(ModItems.SHINY_CHARM);
            if (expEnabled) entries.accept(ModItems.EXP_CHARM);
            if (catchEnabled) entries.accept(ModItems.CATCH_CHARM);
            if (multiEnabled) entries.accept(ModItems.MULTI_CHARM);

            ModItems.TYPE_CHARMS.forEach((type, charm) -> {
                if (Config.isTypeCharmEnabled(type)) entries.accept(charm);
            });
        });
    }
}