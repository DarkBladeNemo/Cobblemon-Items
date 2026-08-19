package com.darkbladenemo.cobblemoncharms.init;

import com.darkbladenemo.cobblemoncharms.CobblemonCharmsMod;
import com.darkbladenemo.cobblemoncharms.common.condition.ConfigCondition;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModConditions {
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS =
            DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, CobblemonCharmsMod.MOD_ID);

    public static final Supplier<MapCodec<ConfigCondition>> CONFIG_CONDITION =
            CONDITION_CODECS.register("config", () -> ConfigCondition.CODEC);
}