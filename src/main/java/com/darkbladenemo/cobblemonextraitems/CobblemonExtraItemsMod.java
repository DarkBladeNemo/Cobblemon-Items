package com.darkbladenemo.cobblemonextraitems;

import com.darkbladenemo.cobblemonextraitems.init.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(CobblemonExtraItemsMod.MOD_ID)
public class CobblemonExtraItemsMod {
    public static final String MOD_ID = "cobblemonextraitems";

    public CobblemonExtraItemsMod(IEventBus modEventBus) {
        // Register items - import the ModItems class
        ModItems.ITEMS.register(modEventBus);
    }
}