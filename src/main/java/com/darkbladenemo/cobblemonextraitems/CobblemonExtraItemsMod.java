package com.darkbladenemo.cobblemonextraitems;

import com.cobblemon.mod.common.api.spawning.spawner.PlayerSpawnerFactory;
import com.darkbladenemo.cobblemonextraitems.config.Config;
import com.darkbladenemo.cobblemonextraitems.event.CharmEvents;
import com.darkbladenemo.cobblemonextraitems.influence.TypeCharmInfluence;
import com.darkbladenemo.cobblemonextraitems.init.ModItems;
import com.darkbladenemo.cobblemonextraitems.item.charm.CharmType;
import com.darkbladenemo.cobblemonextraitems.item.charm.TypeCharm;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Map;

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

            // Add EV & HyperTraining items as before
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

            // Dynamically add all type charms based on config flags
            for (Map.Entry<CharmType, DeferredHolder<Item, TypeCharm>> entry : ModItems.TYPE_CHARMS.entrySet()) {
                CharmType type = entry.getKey();
                DeferredHolder<Item, TypeCharm> charm = entry.getValue();

                boolean enabled = switch (type) {
                    case NORMAL -> Config.ENABLE_NORMAL_CHARM.get();
                    case FIRE -> Config.ENABLE_FIRE_CHARM.get();
                    case WATER -> Config.ENABLE_WATER_CHARM.get();
                    case ELECTRIC -> Config.ENABLE_ELECTRIC_CHARM.get();
                    case GRASS -> Config.ENABLE_GRASS_CHARM.get();
                    case ICE -> Config.ENABLE_ICE_CHARM.get();
                    case FIGHTING -> Config.ENABLE_FIGHTING_CHARM.get();
                    case POISON -> Config.ENABLE_POISON_CHARM.get();
                    case GROUND -> Config.ENABLE_GROUND_CHARM.get();
                    case FLYING -> Config.ENABLE_FLYING_CHARM.get();
                    case PSYCHIC -> Config.ENABLE_PSYCHIC_CHARM.get();
                    case BUG -> Config.ENABLE_BUG_CHARM.get();
                    case ROCK -> Config.ENABLE_ROCK_CHARM.get();
                    case GHOST -> Config.ENABLE_GHOST_CHARM.get();
                    case DRAGON -> Config.ENABLE_DRAGON_CHARM.get();
                    case DARK -> Config.ENABLE_DARK_CHARM.get();
                    case STEEL -> Config.ENABLE_STEEL_CHARM.get();
                    case FAIRY -> Config.ENABLE_FAIRY_CHARM.get();
                };

                if (enabled) {
                    event.accept(charm.get());
                }
            }
        }
    }
}