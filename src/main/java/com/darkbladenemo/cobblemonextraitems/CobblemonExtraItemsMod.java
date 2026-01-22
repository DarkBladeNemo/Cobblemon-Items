package com.darkbladenemo.cobblemonextraitems;

import com.darkbladenemo.cobblemonextraitems.config.Config;
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
        }
    }
}