package com.darkbladenemo.cobblemonextraitems.event

import com.darkbladenemo.cobblemonextraitems.component.ExpCharmData
import com.darkbladenemo.cobblemonextraitems.component.ShinyCharmData
import com.darkbladenemo.cobblemonextraitems.component.TypeCharmData
import com.darkbladenemo.cobblemonextraitems.config.Config
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents
import com.darkbladenemo.cobblemonextraitems.init.ModItems
import com.darkbladenemo.cobblemonextraitems.item.charm.CharmType
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent

object ModEvents {

    @JvmStatic
    @SubscribeEvent
    fun onModifyDefaultComponents(event: ModifyDefaultComponentsEvent) {
        // Set default EXP charm data
        val expMultiplier = Config.EXP_CHARM_MULTIPLIER.get().toFloat()
        event.modify(ModItems.EXP_CHARM.get()) { builder ->
            builder.set(ModDataComponents.EXP_CHARM_DATA.get(), ExpCharmData(expMultiplier))
        }

        // Set default Shiny charm data
        val shinyMultiplier = Config.SHINY_CHARM_MULTIPLIER.get().toFloat()
        event.modify(ModItems.SHINY_CHARM.get()) { builder ->
            builder.set(ModDataComponents.SHINY_CHARM_DATA.get(), ShinyCharmData(shinyMultiplier))
        }

        // Set default Type charm data for all type charms
        val typeMultiplier = Config.TYPE_CHARM_MULTIPLIER.get().toFloat()
        val typeRadius = Config.TYPE_CHARM_RADIUS.get()

        ModItems.TYPE_CHARMS.forEach { (type, deferredCharm) ->
            event.modify(deferredCharm.get()) { builder ->
                builder.set(
                    ModDataComponents.TYPE_CHARM_DATA.get(),
                    TypeCharmData(type, typeMultiplier, typeRadius)
                )
            }
        }
    }
}