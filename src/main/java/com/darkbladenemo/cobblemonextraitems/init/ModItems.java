package com.darkbladenemo.cobblemonextraitems.init;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.darkbladenemo.cobblemonextraitems.CobblemonExtraItemsMod;
import com.darkbladenemo.cobblemonextraitems.item.HighEVItem;
import com.darkbladenemo.cobblemonextraitems.item.HighHyperTrainingItem;
import com.darkbladenemo.cobblemonextraitems.item.charm.*;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.createItems(CobblemonExtraItemsMod.MOD_ID);

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
            () -> new HighHyperTrainingItem(Set.of(Stats.HP)));

    public static final Supplier<Item> HIGH_MIGHTY_CANDY = ITEMS.register("high_mighty_candy",
            () -> new HighHyperTrainingItem(Set.of(Stats.ATTACK)));

    public static final Supplier<Item> HIGH_TOUGH_CANDY = ITEMS.register("high_tough_candy",
            () -> new HighHyperTrainingItem(Set.of(Stats.DEFENCE)));

    public static final Supplier<Item> HIGH_SMART_CANDY = ITEMS.register("high_smart_candy",
            () -> new HighHyperTrainingItem(Set.of(Stats.SPECIAL_ATTACK)));

    public static final Supplier<Item> HIGH_COURAGE_CANDY = ITEMS.register("high_courage_candy",
            () -> new HighHyperTrainingItem(Set.of(Stats.SPECIAL_DEFENCE)));

    public static final Supplier<Item> HIGH_QUICK_CANDY = ITEMS.register("high_quick_candy",
            () -> new HighHyperTrainingItem(Set.of(Stats.SPEED)));

    // Charms
    public static final Supplier<Item> SHINY_CHARM = ITEMS.register("shiny_charm",
            () -> new ShinyCharm());

    public static final Supplier<Item> BUG_CHARM = ITEMS.register("bug_charm",
            () -> new BugCharm());

    public static final Supplier<Item> DARK_CHARM = ITEMS.register("dark_charm",
            () -> new DarkCharm());

    public static final Supplier<Item> DRAGON_CHARM = ITEMS.register("dragon_charm",
            () -> new DragonCharm());

    public static final Supplier<Item> ELECTRIC_CHARM = ITEMS.register("electric_charm",
            () -> new ElectricCharm());

    public static final Supplier<Item> FAIRY_CHARM = ITEMS.register("fairy_charm",
            () -> new FairyCharm());

    public static final Supplier<Item> FIGHTING_CHARM = ITEMS.register("fighting_charm",
            () -> new FightingCharm());

    public static final Supplier<Item> FIRE_CHARM = ITEMS.register("fire_charm",
            () -> new FireCharm());

    public static final Supplier<Item> FLYING_CHARM = ITEMS.register("flying_charm",
            () -> new FlyingCharm());

    public static final Supplier<Item> GHOST_CHARM = ITEMS.register("ghost_charm",
            () -> new GhostCharm());

    public static final Supplier<Item> GRASS_CHARM = ITEMS.register("grass_charm",
            () -> new GrassCharm());

    public static final Supplier<Item> GROUND_CHARM = ITEMS.register("ground_charm",
            () -> new GroundCharm());

    public static final Supplier<Item> ICE_CHARM = ITEMS.register("ice_charm",
            () -> new IceCharm());

    public static final Supplier<Item> NORMAL_CHARM = ITEMS.register("normal_charm",
            () -> new NormalCharm());

    public static final Supplier<Item> POISON_CHARM = ITEMS.register("poison_charm",
            () -> new PoisonCharm());

    public static final Supplier<Item> PSYCHIC_CHARM = ITEMS.register("psychic_charm",
            () -> new PsychicCharm());

    public static final Supplier<Item> ROCK_CHARM = ITEMS.register("rock_charm",
            () -> new RockCharm());

    public static final Supplier<Item> STEEL_CHARM = ITEMS.register("steel_charm",
            () -> new SteelCharm());

    public static final Supplier<Item> WATER_CHARM = ITEMS.register("water_charm",
            () -> new WaterCharm());
}
