package com.darkbladenemo.cobblemonextraitems;

import com.darkbladenemo.cobblemonextraitems.init.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(CobblemonExtraItemsMod.MOD_ID)
public class CobblemonExtraItemsMod {
    public static final String MOD_ID = "cobblemonextraitems";

    public CobblemonExtraItemsMod(IEventBus modEventBus) {
        // Register items - import the ModItems class
        ModItems.ITEMS.register(modEventBus);

        // Register a creative tab addition
        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            // Call .get() on each Supplier to get the Item
            event.accept(ModItems.HIGH_CARBOS.get());
            event.accept(ModItems.HIGH_PROTEIN.get());
            event.accept(ModItems.HIGH_HP_UP.get());
            event.accept(ModItems.HIGH_IRON.get());
            event.accept(ModItems.HIGH_CALCIUM.get());
            event.accept(ModItems.HIGH_ZINC.get());
            event.accept(ModItems.HIGH_HEALTH_CANDY.get());
            event.accept(ModItems.HIGH_MIGHTY_CANDY.get());
            event.accept(ModItems.HIGH_TOUGH_CANDY.get());
            event.accept(ModItems.HIGH_SMART_CANDY.get());
            event.accept(ModItems.HIGH_COURAGE_CANDY.get());
            event.accept(ModItems.HIGH_QUICK_CANDY.get());
        }
    }
}