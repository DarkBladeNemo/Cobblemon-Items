package com.darkbladenemo.cobblemonextraitems.client;

import com.darkbladenemo.cobblemonextraitems.CobblemonExtraItemsMod;
import com.darkbladenemo.cobblemonextraitems.client.keybind.ModKeyBindings;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid = CobblemonExtraItemsMod.MOD_ID, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(ModKeyBindings.OPEN_MULTI_CHARM_GUI);
    }
}