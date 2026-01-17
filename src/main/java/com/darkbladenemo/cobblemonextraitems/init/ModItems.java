package com.darkbladenemo.cobblemonextraitems.init;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.darkbladenemo.cobblemonextraitems.CobblemonExtraItemsMod;
import com.darkbladenemo.cobblemonextraitems.item.HighEVItem;
import com.darkbladenemo.cobblemonextraitems.item.HighHyperTrainingItem;
import kotlin.ranges.IntRange;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
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

    // High IV items
    public static final Supplier<Item> HIGH_HEALTH_CANDY = ITEMS.register("high_health_candy",
            () -> new HighHyperTrainingItem(10, Set.of(Stats.HP), new IntRange(0, 31)));

    public static final Supplier<Item> HIGH_MIGHTY_CANDY = ITEMS.register("high_mighty_candy",
            () -> new HighHyperTrainingItem(10, Set.of(Stats.ATTACK), new IntRange(0, 31)));

    public static final Supplier<Item> HIGH_TOUGH_CANDY = ITEMS.register("high_tough_candy",
            () -> new HighHyperTrainingItem(10, Set.of(Stats.DEFENCE), new IntRange(0, 31)));

    public static final Supplier<Item> HIGH_SMART_CANDY = ITEMS.register("high_smart_candy",
            () -> new HighHyperTrainingItem(10, Set.of(Stats.SPECIAL_ATTACK), new IntRange(0, 31)));

    public static final Supplier<Item> HIGH_COURAGE_CANDY = ITEMS.register("high_courage_candy",
            () -> new HighHyperTrainingItem(10, Set.of(Stats.SPECIAL_DEFENCE), new IntRange(0, 31)));

    public static final Supplier<Item> HIGH_QUICK_CANDY = ITEMS.register("high_quick_candy",
            () -> new HighHyperTrainingItem(10, Set.of(Stats.SPEED), new IntRange(0, 31)));
}