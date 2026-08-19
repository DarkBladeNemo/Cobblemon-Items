package com.darkbladenemo.cobblemoncharms.init;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.darkbladenemo.cobblemoncharms.CobblemonCharmsMod;
import com.darkbladenemo.cobblemoncharms.common.config.Config;
import com.darkbladenemo.cobblemoncharms.common.item.EVBoostItem;
import com.darkbladenemo.cobblemoncharms.common.item.IVBoostItem;
import com.darkbladenemo.cobblemoncharms.common.item.charm.*;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.createItems(CobblemonCharmsMod.MOD_ID);

    // Map to store type charms
    public static final Map<CharmType, DeferredHolder<Item, TypeCharm>> TYPE_CHARMS =
            new EnumMap<>(CharmType.class);


    // EV Items
    public static final Supplier<Item> SUPER_CARBOS = ITEMS.register("super_carbos",
            () -> new EVBoostItem(Stats.SPEED,
                    () -> Config.ENABLE_ALL_EV_ITEMS.get() && Config.ENABLE_SUPER_CARBOS.get()));

    public static final Supplier<Item> SUPER_PROTEIN = ITEMS.register("super_protein",
            () -> new EVBoostItem(Stats.ATTACK,
                    () -> Config.ENABLE_ALL_EV_ITEMS.get() && Config.ENABLE_SUPER_PROTEIN.get()));

    public static final Supplier<Item> SUPER_HP_UP = ITEMS.register("super_hp_up",
            () -> new EVBoostItem(Stats.HP,
                    () -> Config.ENABLE_ALL_EV_ITEMS.get() && Config.ENABLE_SUPER_HP_UP.get()));

    public static final Supplier<Item> SUPER_IRON = ITEMS.register("super_iron",
            () -> new EVBoostItem(Stats.DEFENCE,
                    () -> Config.ENABLE_ALL_EV_ITEMS.get() && Config.ENABLE_SUPER_IRON.get()));

    public static final Supplier<Item> SUPER_CALCIUM = ITEMS.register("super_calcium",
            () -> new EVBoostItem(Stats.SPECIAL_ATTACK,
                    () -> Config.ENABLE_ALL_EV_ITEMS.get() && Config.ENABLE_SUPER_CALCIUM.get()));

    public static final Supplier<Item> SUPER_ZINC = ITEMS.register("super_zinc",
            () -> new EVBoostItem(Stats.SPECIAL_DEFENCE,
                    () -> Config.ENABLE_ALL_EV_ITEMS.get() && Config.ENABLE_SUPER_ZINC.get()));

    // super IV items - configurable amount
    public static final Supplier<Item> SUPER_HEALTH_CANDY = ITEMS.register("super_health_candy",
            () -> new IVBoostItem(Set.of(Stats.HP),
                    () -> Config.ENABLE_ALL_IV_ITEMS.get() && Config.ENABLE_SUPER_HEALTH_CANDY.get()));

    public static final Supplier<Item> SUPER_MIGHTY_CANDY = ITEMS.register("super_mighty_candy",
            () -> new IVBoostItem(Set.of(Stats.ATTACK),
                    () -> Config.ENABLE_ALL_IV_ITEMS.get() && Config.ENABLE_SUPER_MIGHTY_CANDY.get()));

    public static final Supplier<Item> SUPER_TOUGH_CANDY = ITEMS.register("super_tough_candy",
            () -> new IVBoostItem(Set.of(Stats.DEFENCE),
                    () -> Config.ENABLE_ALL_IV_ITEMS.get() && Config.ENABLE_SUPER_TOUGH_CANDY.get()));

    public static final Supplier<Item> SUPER_SMART_CANDY = ITEMS.register("super_smart_candy",
            () -> new IVBoostItem(Set.of(Stats.SPECIAL_ATTACK),
                    () -> Config.ENABLE_ALL_IV_ITEMS.get() && Config.ENABLE_SUPER_SMART_CANDY.get()));

    public static final Supplier<Item> SUPER_COURAGE_CANDY = ITEMS.register("super_courage_candy",
            () -> new IVBoostItem(Set.of(Stats.SPECIAL_DEFENCE),
                    () -> Config.ENABLE_ALL_IV_ITEMS.get() && Config.ENABLE_SUPER_COURAGE_CANDY.get()));

    public static final Supplier<Item> SUPER_QUICK_CANDY = ITEMS.register("super_quick_candy",
            () -> new IVBoostItem(Set.of(Stats.SPEED),
                    () -> Config.ENABLE_ALL_IV_ITEMS.get() && Config.ENABLE_SUPER_QUICK_CANDY.get()));

    public static final Supplier<Item> GOLD_BOTTLE_CAP = ITEMS.register("gold_bottle_cap",
            () -> new IVBoostItem(Set.of(
                    Stats.HP,
                    Stats.ATTACK,
                    Stats.DEFENCE,
                    Stats.SPECIAL_ATTACK,
                    Stats.SPECIAL_DEFENCE,
                    Stats.SPEED),
                    () -> Config.ENABLE_ALL_IV_ITEMS.get() && Config.ENABLE_GOLD_BOTTLE_CAP.get()));

    // Charms
    public static final Supplier<Item> SHINY_CHARM = ITEMS.register("shiny_charm",
            ShinyCharm::new);

    public static final Supplier<Item> EXP_CHARM = ITEMS.register("exp_charm",
            ExpCharm::new);

    public static final Supplier<Item> CATCH_CHARM = ITEMS.register("catch_charm",
            CatchCharm::new);

    public static final Supplier<Item> MULTI_CHARM = ITEMS.register("multi_charm",
            MultiCharm::new);

    // Static initializer block to register type charms
    static {
        // Register all type charms
        for (CharmType type : CharmType.getEntries()) {
            String name = type.getTranslationKey() + "_charm";
            DeferredHolder<Item, TypeCharm> charm = ITEMS.register(name,
                    () -> new TypeCharm(type));
            TYPE_CHARMS.put(type, charm);
        }
    }
}