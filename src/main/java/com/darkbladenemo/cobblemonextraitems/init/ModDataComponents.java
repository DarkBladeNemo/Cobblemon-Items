package com.darkbladenemo.cobblemonextraitems.init;

import com.darkbladenemo.cobblemonextraitems.CobblemonExtraItemsMod;
import com.darkbladenemo.cobblemonextraitems.component.ExpCharmData;
import com.darkbladenemo.cobblemonextraitems.component.ShinyCharmData;
import com.darkbladenemo.cobblemonextraitems.component.TypeCharmData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, CobblemonExtraItemsMod.MOD_ID);

    public static final Supplier<DataComponentType<ExpCharmData>> EXP_CHARM_DATA =
            DATA_COMPONENTS.register("exp_charm_data", () ->
                    DataComponentType.<ExpCharmData>builder()
                            .persistent(ExpCharmData.CODEC)
                            .build()
            );

    public static final Supplier<DataComponentType<ShinyCharmData>> SHINY_CHARM_DATA =
            DATA_COMPONENTS.register("shiny_charm_data", () ->
                    DataComponentType.<ShinyCharmData>builder()
                            .persistent(ShinyCharmData.CODEC)
                            .build()
            );

    public static final Supplier<DataComponentType<TypeCharmData>> TYPE_CHARM_DATA =
            DATA_COMPONENTS.register("type_charm_data", () ->
                    DataComponentType.<TypeCharmData>builder()
                            .persistent(TypeCharmData.CODEC)
                            .build()
            );
}