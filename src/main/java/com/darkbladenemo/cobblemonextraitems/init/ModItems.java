package com.darkbladenemo.cobblemonextraitems.init;

import com.darkbladenemo.cobblemonextraitems.CobblemonExtraItemsMod;
import com.darkbladenemo.cobblemonextraitems.item.HighEVItem;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.createItems(CobblemonExtraItemsMod.MOD_ID);

    public static final Supplier<Item> HIGH_CARBOS = ITEMS.register("high_carbos",
            () -> new HighEVItem(Stats.SPEED));

    public static final Supplier<Item> HIGH_PROTEIN = ITEMS.register("high_protein",
            () -> new HighEVItem(Stats.ATTACK));

    public static final Supplier<Item> HIGH_HP_UP = ITEMS.register("high_hp_up",
            () -> new HighEVItem(Stats.HP));

    public static final Supplier<Item> HIGH_IRON = ITEMS.register("high_iron",
            () -> new HighEVItem(Stats.DEFENCE));

    public static final Supplier<Item> HIGH_CALCIUM = ITEMS.register("high_calcium",
            () -> new HighEVItem(Stats.SPECIAL_ATTACK));

    public static final Supplier<Item> HIGH_ZINC = ITEMS.register("high_zinc",
            () -> new HighEVItem(Stats.SPECIAL_DEFENCE));
}