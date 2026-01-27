package com.darkbladenemo.cobblemonextraitems.init;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.darkbladenemo.cobblemonextraitems.CobblemonExtraItemsMod;
import com.darkbladenemo.cobblemonextraitems.common.item.HighEVItem;
import com.darkbladenemo.cobblemonextraitems.common.item.IVBoostItem;
import com.darkbladenemo.cobblemonextraitems.common.item.charm.*;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.createItems(CobblemonExtraItemsMod.MOD_ID);

    // Map to store type charms
    public static final Map<CharmType, DeferredHolder<Item, TypeCharm>> TYPE_CHARMS =
            new EnumMap<>(CharmType.class);


    // EV Items - always 100 EVs (not configurable)
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

    // High IV items - configurable amount
    public static final Supplier<Item> HIGH_HEALTH_CANDY = ITEMS.register("high_health_candy",
            () -> new IVBoostItem(Set.of(Stats.HP)));

    public static final Supplier<Item> HIGH_MIGHTY_CANDY = ITEMS.register("high_mighty_candy",
            () -> new IVBoostItem(Set.of(Stats.ATTACK)));

    public static final Supplier<Item> HIGH_TOUGH_CANDY = ITEMS.register("high_tough_candy",
            () -> new IVBoostItem(Set.of(Stats.DEFENCE)));

    public static final Supplier<Item> HIGH_SMART_CANDY = ITEMS.register("high_smart_candy",
            () -> new IVBoostItem(Set.of(Stats.SPECIAL_ATTACK)));

    public static final Supplier<Item> HIGH_COURAGE_CANDY = ITEMS.register("high_courage_candy",
            () -> new IVBoostItem(Set.of(Stats.SPECIAL_DEFENCE)));

    public static final Supplier<Item> HIGH_QUICK_CANDY = ITEMS.register("high_quick_candy",
            () -> new IVBoostItem(Set.of(Stats.SPEED)));

    public static final Supplier<Item> GOLD_BOTTLE_CAP = ITEMS.register("gold_bottle_cap",
            () -> new IVBoostItem(Set.of(
                    Stats.HP,
                    Stats.ATTACK,
                    Stats.DEFENCE,
                    Stats.SPECIAL_ATTACK,
                    Stats.SPECIAL_DEFENCE,
                    Stats.SPEED
            )));

    // Charms
    public static final Supplier<Item> SHINY_CHARM = ITEMS.register("shiny_charm",
            () -> new ShinyCharm());

    public static final Supplier<Item> EXP_CHARM = ITEMS.register("exp_charm",
            () -> new ExpCharm());

    public static final Supplier<Item> MULTI_CHARM = ITEMS.register("multi_charm",
            () -> new MultiCharm());

    // Static initializer block to register type charms
    static {
        // Register all type charms
        for (CharmType type : CharmType.values()) {
            String name = type.getTranslationKey() + "_charm";
            DeferredHolder<Item, TypeCharm> charm = ITEMS.register(name,
                    () -> new TypeCharm(type));
            TYPE_CHARMS.put(type, charm);
        }
    }

    // Helper method to get a type charm
    public static TypeCharm getTypeCharm(CharmType type) {
        DeferredHolder<Item, TypeCharm> deferred = TYPE_CHARMS.get(type);
        return deferred != null ? deferred.get() : null;
    }
}