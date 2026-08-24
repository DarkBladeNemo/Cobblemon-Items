package com.darkbladenemo.cobblemoncharms;

import com.cobblemon.mod.common.api.spawning.spawner.PlayerSpawnerFactory;
import com.darkbladenemo.cobblemoncharms.command.CCharmsCommand;
import com.darkbladenemo.cobblemoncharms.common.config.Config;
import com.darkbladenemo.cobblemoncharms.common.event.*;
import com.darkbladenemo.cobblemoncharms.common.influence.ShinyCharmInfluence;
import com.darkbladenemo.cobblemoncharms.common.influence.TypeCharmInfluence;
import com.darkbladenemo.cobblemoncharms.init.ModConditions;
import com.darkbladenemo.cobblemoncharms.init.ModDataComponents;
import com.darkbladenemo.cobblemoncharms.init.ModItems;
import com.darkbladenemo.cobblemoncharms.init.ModRecipes;
import com.darkbladenemo.cobblemoncharms.common.item.charm.CharmType;
import com.darkbladenemo.cobblemoncharms.common.item.charm.TypeCharm;
import com.darkbladenemo.cobblemoncharms.tick.TickManager;
import com.darkbladenemo.cobblemoncharms.utils.PokedexRegionUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Map;

@Mod(CobblemonCharmsMod.MOD_ID)
public class CobblemonCharmsMod {
    public static final String MOD_ID = "cobblemoncharms";

    public CobblemonCharmsMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register config
        ModConfigSpec configSpec = Config.SPEC;
        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, configSpec);

        // Register items
        ModItems.ITEMS.register(modEventBus);

        // Register components
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
        ModConditions.CONDITION_CODECS.register(modEventBus);
        modEventBus.addListener(ModEvents::onModifyDefaultComponents);

        // Register recipes
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);

        // Register events
        ShinyCharmEvents.register();
        ExpCharmEvents.register();
        CatchCharmEvents.register();
        AdvancementRewardHandler.register();
        TypeCharmAdvancementEvents.register();
        PokedexRegionUtils.register();

        // Register tick manager
        TickManager.register();
        NeoForge.EVENT_BUS.addListener(CobblemonCharmsMod::onRegisterCommands);

        // Register charm influence
        PlayerSpawnerFactory.INSTANCE.getInfluenceBuilders().add(TypeCharmInfluence::new);
        PlayerSpawnerFactory.INSTANCE.getInfluenceBuilders().add(ShinyCharmInfluence::new);

        // Register a creative tab addition
        modEventBus.addListener(this::addCreative);

    }

    private static void onRegisterCommands(RegisterCommandsEvent event) {
        CCharmsCommand.register(event.getDispatcher());
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {

            // EV Items
            if(Config.ENABLE_ALL_EV_ITEMS.get()){
                if (Config.ENABLE_SUPER_CARBOS.get()) event.accept(ModItems.SUPER_CARBOS.get());
                if (Config.ENABLE_SUPER_PROTEIN.get()) event.accept(ModItems.SUPER_PROTEIN.get());
                if (Config.ENABLE_SUPER_HP_UP.get()) event.accept(ModItems.SUPER_HP_UP.get());
                if (Config.ENABLE_SUPER_IRON.get()) event.accept(ModItems.SUPER_IRON.get());
                if (Config.ENABLE_SUPER_CALCIUM.get()) event.accept(ModItems.SUPER_CALCIUM.get());
                if (Config.ENABLE_SUPER_ZINC.get()) event.accept(ModItems.SUPER_ZINC.get());
            }

            // IV Items
            if(Config.ENABLE_ALL_IV_ITEMS.get()){
                if (Config.ENABLE_SUPER_HEALTH_CANDY.get()) event.accept(ModItems.SUPER_HEALTH_CANDY.get());
                if (Config.ENABLE_SUPER_MIGHTY_CANDY.get()) event.accept(ModItems.SUPER_MIGHTY_CANDY.get());
                if (Config.ENABLE_SUPER_TOUGH_CANDY.get()) event.accept(ModItems.SUPER_TOUGH_CANDY.get());
                if (Config.ENABLE_SUPER_SMART_CANDY.get()) event.accept(ModItems.SUPER_SMART_CANDY.get());
                if (Config.ENABLE_SUPER_COURAGE_CANDY.get()) event.accept(ModItems.SUPER_COURAGE_CANDY.get());
                if (Config.ENABLE_SUPER_QUICK_CANDY.get()) event.accept(ModItems.SUPER_QUICK_CANDY.get());
                if (Config.ENABLE_GOLD_BOTTLE_CAP.get()) event.accept(ModItems.GOLD_BOTTLE_CAP.get());
            }

            // Other Charms
            if (Config.ENABLE_SHINY_CHARM.get()) event.accept(ModItems.SHINY_CHARM.get());
            if (Config.ENABLE_EXP_CHARM.get()) event.accept(ModItems.EXP_CHARM.get());
            if (Config.ENABLE_CATCH_CHARM.get()) event.accept(ModItems.CATCH_CHARM.get());
            if (Config.ENABLE_MULTI_CHARM.get()) event.accept(ModItems.MULTI_CHARM.get());

            // Type Charms
            for (Map.Entry<CharmType, DeferredHolder<Item, TypeCharm>> entry : ModItems.TYPE_CHARMS.entrySet()) {
                if (Config.isTypeCharmEnabled(entry.getKey())) {
                    event.accept(entry.getValue().get());
                }
            }
        }
    }
}