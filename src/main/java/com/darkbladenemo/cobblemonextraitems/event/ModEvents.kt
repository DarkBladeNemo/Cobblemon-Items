package com.darkbladenemo.cobblemonextraitems.event

import com.darkbladenemo.cobblemonextraitems.component.EVItemData
import com.darkbladenemo.cobblemonextraitems.component.ExpCharmData
import com.darkbladenemo.cobblemonextraitems.component.IVItemData
import com.darkbladenemo.cobblemonextraitems.component.MultiCharmData
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

        // Set default Multi charm data (empty)
        event.modify(ModItems.MULTI_CHARM.get()) { builder ->
            builder.set(ModDataComponents.MULTI_CHARM_DATA.get(), MultiCharmData.empty())
        }

        // Set default EV item data
        val evAmount = Config.HIGH_EV_INCREASE_AMOUNT.get()

        event.modify(ModItems.HIGH_CARBOS.get()) { builder ->
            builder.set(ModDataComponents.EV_ITEM_DATA.get(), EVItemData("speed", evAmount))
        }
        event.modify(ModItems.HIGH_PROTEIN.get()) { builder ->
            builder.set(ModDataComponents.EV_ITEM_DATA.get(), EVItemData("attack", evAmount))
        }
        event.modify(ModItems.HIGH_HP_UP.get()) { builder ->
            builder.set(ModDataComponents.EV_ITEM_DATA.get(), EVItemData("hp", evAmount))
        }
        event.modify(ModItems.HIGH_IRON.get()) { builder ->
            builder.set(ModDataComponents.EV_ITEM_DATA.get(), EVItemData("defence", evAmount))
        }
        event.modify(ModItems.HIGH_CALCIUM.get()) { builder ->
            builder.set(ModDataComponents.EV_ITEM_DATA.get(), EVItemData("special_attack", evAmount))
        }
        event.modify(ModItems.HIGH_ZINC.get()) { builder ->
            builder.set(ModDataComponents.EV_ITEM_DATA.get(), EVItemData("special_defence", evAmount))
        }

        // Set default IV item data
        val ivAmount = Config.HIGH_IV_INCREASE_AMOUNT.get()

        event.modify(ModItems.HIGH_HEALTH_CANDY.get()) { builder ->
            builder.set(ModDataComponents.IV_ITEM_DATA.get(), IVItemData(listOf("hp"), ivAmount))
        }
        event.modify(ModItems.HIGH_MIGHTY_CANDY.get()) { builder ->
            builder.set(ModDataComponents.IV_ITEM_DATA.get(), IVItemData(listOf("attack"), ivAmount))
        }
        event.modify(ModItems.HIGH_TOUGH_CANDY.get()) { builder ->
            builder.set(ModDataComponents.IV_ITEM_DATA.get(), IVItemData(listOf("defence"), ivAmount))
        }
        event.modify(ModItems.HIGH_SMART_CANDY.get()) { builder ->
            builder.set(ModDataComponents.IV_ITEM_DATA.get(), IVItemData(listOf("special_attack"), ivAmount))
        }
        event.modify(ModItems.HIGH_COURAGE_CANDY.get()) { builder ->
            builder.set(ModDataComponents.IV_ITEM_DATA.get(), IVItemData(listOf("special_defence"), ivAmount))
        }
        event.modify(ModItems.HIGH_QUICK_CANDY.get()) { builder ->
            builder.set(ModDataComponents.IV_ITEM_DATA.get(), IVItemData(listOf("speed"), ivAmount))
        }
    }
}