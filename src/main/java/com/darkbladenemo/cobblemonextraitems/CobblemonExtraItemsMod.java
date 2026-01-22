package com.darkbladenemo.cobblemonextraitems;

import com.cobblemon.mod.common.api.spawning.spawner.PlayerSpawnerFactory;
import com.darkbladenemo.cobblemonextraitems.config.Config;
import com.darkbladenemo.cobblemonextraitems.event.CharmEvents;
import com.darkbladenemo.cobblemonextraitems.influence.TypeCharmInfluence;
import com.darkbladenemo.cobblemonextraitems.init.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(CobblemonExtraItemsMod.MOD_ID)
public class CobblemonExtraItemsMod {
    public static final String MOD_ID = "cobblemonextraitems";

    public CobblemonExtraItemsMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register config
        ModConfigSpec configSpec = Config.SPEC;
        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, configSpec);

        // Register items
        ModItems.ITEMS.register(modEventBus);

        // Register events
        CharmEvents.register();

        // Register type charm influence
        PlayerSpawnerFactory.INSTANCE.getInfluenceBuilders().add(TypeCharmInfluence::new);

        // Register a creative tab addition
        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            // Check config at runtime before adding to the creative tab
            if (Config.ENABLE_HIGH_CARBOS.get()) event.accept(ModItems.HIGH_CARBOS.get());
            if (Config.ENABLE_HIGH_PROTEIN.get()) event.accept(ModItems.HIGH_PROTEIN.get());
            if (Config.ENABLE_HIGH_HP_UP.get()) event.accept(ModItems.HIGH_HP_UP.get());
            if (Config.ENABLE_HIGH_IRON.get()) event.accept(ModItems.HIGH_IRON.get());
            if (Config.ENABLE_HIGH_CALCIUM.get()) event.accept(ModItems.HIGH_CALCIUM.get());
            if (Config.ENABLE_HIGH_ZINC.get()) event.accept(ModItems.HIGH_ZINC.get());
            if (Config.ENABLE_HIGH_HEALTH_CANDY.get()) event.accept(ModItems.HIGH_HEALTH_CANDY.get());
            if (Config.ENABLE_HIGH_MIGHTY_CANDY.get()) event.accept(ModItems.HIGH_MIGHTY_CANDY.get());
            if (Config.ENABLE_HIGH_TOUGH_CANDY.get()) event.accept(ModItems.HIGH_TOUGH_CANDY.get());
            if (Config.ENABLE_HIGH_SMART_CANDY.get()) event.accept(ModItems.HIGH_SMART_CANDY.get());
            if (Config.ENABLE_HIGH_COURAGE_CANDY.get()) event.accept(ModItems.HIGH_COURAGE_CANDY.get());
            if (Config.ENABLE_HIGH_QUICK_CANDY.get()) event.accept(ModItems.HIGH_QUICK_CANDY.get());
            if (Config.ENABLE_SHINY_CHARM.get()) event.accept(ModItems.SHINY_CHARM.get());
            if (Config.ENABLE_BUG_CHARM.get()) event.accept(ModItems.BUG_CHARM.get());
            if (Config.ENABLE_DARK_CHARM.get()) event.accept(ModItems.DARK_CHARM.get());
            if (Config.ENABLE_DRAGON_CHARM.get()) event.accept(ModItems.DRAGON_CHARM.get());
            if (Config.ENABLE_ELECTRIC_CHARM.get()) event.accept(ModItems.ELECTRIC_CHARM.get());
            if (Config.ENABLE_FAIRY_CHARM.get()) event.accept(ModItems.FAIRY_CHARM.get());
            if (Config.ENABLE_FIGHTING_CHARM.get()) event.accept(ModItems.FIGHTING_CHARM.get());
            if (Config.ENABLE_FIRE_CHARM.get()) event.accept(ModItems.FIRE_CHARM.get());
            if (Config.ENABLE_FLYING_CHARM.get()) event.accept(ModItems.FLYING_CHARM.get());
            if (Config.ENABLE_GHOST_CHARM.get()) event.accept(ModItems.GHOST_CHARM.get());
            if (Config.ENABLE_GRASS_CHARM.get()) event.accept(ModItems.GRASS_CHARM.get());
            if (Config.ENABLE_GROUND_CHARM.get()) event.accept(ModItems.GROUND_CHARM.get());
            if (Config.ENABLE_ICE_CHARM.get()) event.accept(ModItems.ICE_CHARM.get());
            if (Config.ENABLE_NORMAL_CHARM.get()) event.accept(ModItems.NORMAL_CHARM.get());
            if (Config.ENABLE_POISON_CHARM.get()) event.accept(ModItems.POISON_CHARM.get());
            if (Config.ENABLE_PSYCHIC_CHARM.get()) event.accept(ModItems.PSYCHIC_CHARM.get());
            if (Config.ENABLE_ROCK_CHARM.get()) event.accept(ModItems.ROCK_CHARM.get());
            if (Config.ENABLE_STEEL_CHARM.get()) event.accept(ModItems.STEEL_CHARM.get());
            if (Config.ENABLE_WATER_CHARM.get()) event.accept(ModItems.WATER_CHARM.get());
        }
    }
}