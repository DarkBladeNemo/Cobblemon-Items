package com.darkbladenemo.cobblemoncharms.client;

import com.darkbladenemo.cobblemoncharms.CobblemonCharmsMod;
import com.darkbladenemo.cobblemoncharms.client.keybind.ModKeyBindings;
import com.darkbladenemo.cobblemoncharms.client.network.ClientPacketHandlers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = CobblemonCharmsMod.MOD_ID, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(ModKeyBindings.OPEN_MULTI_CHARM_GUI);
        NeoForge.EVENT_BUS.addListener((ClientPlayerNetworkEvent.LoggingOut e) ->
                ClientPacketHandlers.onDisconnect()
        );
    }
}