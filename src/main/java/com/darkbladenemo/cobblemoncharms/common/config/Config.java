package com.darkbladenemo.cobblemoncharms.common.config;

import com.darkbladenemo.cobblemoncharms.common.item.charm.CharmType;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.EnumMap;
import java.util.Map;

public class Config {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    // Quick toggles
    public static final ModConfigSpec.BooleanValue ENABLE_ALL_EV_ITEMS;
    public static final ModConfigSpec.BooleanValue ENABLE_ALL_IV_ITEMS;
    public static final ModConfigSpec.BooleanValue ENABLE_ALL_TYPE_CHARMS;
    public static final ModConfigSpec.BooleanValue GRANT_CHARM_ON_ADVANCEMENT;

    // Global charm settings
    public static final ModConfigSpec.BooleanValue CHARM_EFFECT_REQUIRES_ADVANCEMENT;

    // Shiny Charm
    public static final ModConfigSpec.BooleanValue ENABLE_SHINY_CHARM;
    public static final ModConfigSpec.BooleanValue SHINY_CHARM_AFFECTS_HONEY_LOG;
    public static final ModConfigSpec.BooleanValue GRANT_SHINY_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.DoubleValue SHINY_CHARM_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue SHINY_CHARM_DEX_THRESHOLD;

    // EXP Charm
    public static final ModConfigSpec.BooleanValue ENABLE_EXP_CHARM;
    public static final ModConfigSpec.BooleanValue GRANT_EXP_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.DoubleValue EXP_CHARM_MULTIPLIER;

    // Catch Charm
    public static final ModConfigSpec.BooleanValue ENABLE_CATCH_CHARM;
    public static final ModConfigSpec.BooleanValue GRANT_CATCH_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.DoubleValue CATCH_CHARM_MULTIPLIER;

    // Multi Charm
    public static final ModConfigSpec.BooleanValue ENABLE_MULTI_CHARM;

    // Type Charm Configuration
    public static final ModConfigSpec.DoubleValue TYPE_CHARM_RADIUS;
    public static final ModConfigSpec.DoubleValue TYPE_CHARM_MATCH_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue TYPE_CHARM_NON_MATCH_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue TYPE_CHARM_THRESHOLD_PERCENTAGE;

    // Individual type charm toggles
    private static final Map<CharmType, ModConfigSpec.BooleanValue> TYPE_CHARM_CONFIG_MAP =
            new EnumMap<>(CharmType.class);
    public static final ModConfigSpec.BooleanValue ENABLE_BUG_CHARM;
    public static final ModConfigSpec.BooleanValue ENABLE_DARK_CHARM;
    public static final ModConfigSpec.BooleanValue ENABLE_DRAGON_CHARM;
    public static final ModConfigSpec.BooleanValue ENABLE_ELECTRIC_CHARM;
    public static final ModConfigSpec.BooleanValue ENABLE_FAIRY_CHARM;
    public static final ModConfigSpec.BooleanValue ENABLE_FIGHTING_CHARM;
    public static final ModConfigSpec.BooleanValue ENABLE_FIRE_CHARM;
    public static final ModConfigSpec.BooleanValue ENABLE_FLYING_CHARM;
    public static final ModConfigSpec.BooleanValue ENABLE_GHOST_CHARM;
    public static final ModConfigSpec.BooleanValue ENABLE_GRASS_CHARM;
    public static final ModConfigSpec.BooleanValue ENABLE_GROUND_CHARM;
    public static final ModConfigSpec.BooleanValue ENABLE_ICE_CHARM;
    public static final ModConfigSpec.BooleanValue ENABLE_NORMAL_CHARM;
    public static final ModConfigSpec.BooleanValue ENABLE_POISON_CHARM;
    public static final ModConfigSpec.BooleanValue ENABLE_PSYCHIC_CHARM;
    public static final ModConfigSpec.BooleanValue ENABLE_ROCK_CHARM;
    public static final ModConfigSpec.BooleanValue ENABLE_STEEL_CHARM;
    public static final ModConfigSpec.BooleanValue ENABLE_WATER_CHARM;

    // Individual type charm grant on advancement toggles
    private static final Map<CharmType, ModConfigSpec.BooleanValue> TYPE_CHARM_GRANT_MAP =
            new EnumMap<>(CharmType.class);
    public static final ModConfigSpec.BooleanValue GRANT_BUG_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.BooleanValue GRANT_DARK_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.BooleanValue GRANT_DRAGON_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.BooleanValue GRANT_ELECTRIC_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.BooleanValue GRANT_FAIRY_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.BooleanValue GRANT_FIGHTING_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.BooleanValue GRANT_FIRE_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.BooleanValue GRANT_FLYING_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.BooleanValue GRANT_GHOST_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.BooleanValue GRANT_GRASS_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.BooleanValue GRANT_GROUND_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.BooleanValue GRANT_ICE_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.BooleanValue GRANT_NORMAL_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.BooleanValue GRANT_POISON_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.BooleanValue GRANT_PSYCHIC_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.BooleanValue GRANT_ROCK_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.BooleanValue GRANT_STEEL_CHARM_ON_ADVANCEMENT;
    public static final ModConfigSpec.BooleanValue GRANT_WATER_CHARM_ON_ADVANCEMENT;

    // EV and IV increase amounts
    public static final ModConfigSpec.IntValue SUPER_EV_INCREASE_AMOUNT;
    public static final ModConfigSpec.IntValue SUPER_IV_INCREASE_AMOUNT;
    public static final ModConfigSpec.IntValue GOLD_BOTTLE_CAP_IV_AMOUNT;

    // Individual EV item toggles
    public static final ModConfigSpec.BooleanValue ENABLE_SUPER_CARBOS;
    public static final ModConfigSpec.BooleanValue ENABLE_SUPER_PROTEIN;
    public static final ModConfigSpec.BooleanValue ENABLE_SUPER_HP_UP;
    public static final ModConfigSpec.BooleanValue ENABLE_SUPER_IRON;
    public static final ModConfigSpec.BooleanValue ENABLE_SUPER_CALCIUM;
    public static final ModConfigSpec.BooleanValue ENABLE_SUPER_ZINC;

    // Individual IV item toggles
    public static final ModConfigSpec.BooleanValue ENABLE_GOLD_BOTTLE_CAP;
    public static final ModConfigSpec.BooleanValue ENABLE_SUPER_HEALTH_CANDY;
    public static final ModConfigSpec.BooleanValue ENABLE_SUPER_MIGHTY_CANDY;
    public static final ModConfigSpec.BooleanValue ENABLE_SUPER_TOUGH_CANDY;
    public static final ModConfigSpec.BooleanValue ENABLE_SUPER_SMART_CANDY;
    public static final ModConfigSpec.BooleanValue ENABLE_SUPER_COURAGE_CANDY;
    public static final ModConfigSpec.BooleanValue ENABLE_SUPER_QUICK_CANDY;

    static {
        BUILDER.push("Quick Toggles");
        ENABLE_ALL_EV_ITEMS = BUILDER
                .comment("Master toggle for all EV items (overrides individual enables)")
                .define("enable_all_ev_items", false);
        ENABLE_ALL_IV_ITEMS = BUILDER
                .comment("Master toggle for all IV items (overrides individual enables)")
                .define("enable_all_iv_items", false);
        ENABLE_ALL_TYPE_CHARMS = BUILDER
                .comment("Master toggle for all type charms (overrides individual enables)")
                .define("enable_all_type_charms", true);
        GRANT_CHARM_ON_ADVANCEMENT = BUILDER
                .comment("If true, charms will be granted when a charm related advancement is earned")
                .comment("Acts as a master toggle — if false, no charms are granted regardless of per-type settings")
                .define("grant_charm_on_advancement", true);
        BUILDER.pop();

        BUILDER.push("Training Items");

        BUILDER.push("EV Items");
        SUPER_EV_INCREASE_AMOUNT = BUILDER
                .comment("Amount of EVs added by super EV items (default: 100, range: 1-252)")
                .defineInRange("super_ev_increase_amount", 100, 1, 252);
        ENABLE_SUPER_CARBOS  = BUILDER.define("enable_super_carbos", true);
        ENABLE_SUPER_PROTEIN = BUILDER.define("enable_super_protein", true);
        ENABLE_SUPER_HP_UP   = BUILDER.define("enable_super_hp_up", true);
        ENABLE_SUPER_IRON    = BUILDER.define("enable_super_iron", true);
        ENABLE_SUPER_CALCIUM = BUILDER.define("enable_super_calcium", true);
        ENABLE_SUPER_ZINC    = BUILDER.define("enable_super_zinc", true);
        BUILDER.pop();

        BUILDER.push("IV Items");
        SUPER_IV_INCREASE_AMOUNT = BUILDER
                .comment("Amount of IVs added by super IV candies (default: 10, range: 1-31)")
                .defineInRange("super_iv_increase_amount", 10, 1, 31);
        GOLD_BOTTLE_CAP_IV_AMOUNT = BUILDER
                .comment("Amount of IVs added by Gold Bottle Cap to ALL stats")
                .comment("Default: 31 (instantly max out all IVs)")
                .defineInRange("gold_bottle_cap_iv_amount", 31, 1, 31);
        ENABLE_SUPER_HEALTH_CANDY  = BUILDER.define("enable_super_health_candy", true);
        ENABLE_SUPER_MIGHTY_CANDY  = BUILDER.define("enable_super_mighty_candy", true);
        ENABLE_SUPER_TOUGH_CANDY   = BUILDER.define("enable_super_tough_candy", true);
        ENABLE_SUPER_SMART_CANDY   = BUILDER.define("enable_super_smart_candy", true);
        ENABLE_SUPER_COURAGE_CANDY = BUILDER.define("enable_super_courage_candy", true);
        ENABLE_SUPER_QUICK_CANDY   = BUILDER.define("enable_super_quick_candy", true);
        ENABLE_GOLD_BOTTLE_CAP = BUILDER
                .comment("Enable Gold Bottle Cap (increases all IVs)")
                .define("enable_gold_bottle_cap", true);
        BUILDER.pop();

        BUILDER.pop();

        BUILDER.push("Charms");
        CHARM_EFFECT_REQUIRES_ADVANCEMENT = BUILDER
                .comment("If true, charms have no effect until the player earns the corresponding advancement")
                .define("charm_effect_requires_advancement", true);

        BUILDER.push("Shiny Charm");
        ENABLE_SHINY_CHARM = BUILDER
                .comment("Enable Shiny Charm item")
                .define("enable_shiny_charm", true);
        SHINY_CHARM_AFFECTS_HONEY_LOG = BUILDER
                .comment("If true, the Shiny Charm also boosts shiny odds from honey-slathered saccharine logs")
                .define("shiny_charm_affects_honey_log", true);
        GRANT_SHINY_CHARM_ON_ADVANCEMENT = BUILDER
                .comment("If true, the Shiny Charm will be given as an item reward when its advancement is earned")
                .comment("Has no effect if grant_charm_on_advancement is false")
                .define("grant_shiny_charm_on_advancement", true);
        SHINY_CHARM_MULTIPLIER = BUILDER
                .comment("Shiny Charm multiplier (default: 3.0 = 3x better shiny odds)")
                .comment("Cobblemon base rate: 1 in 8192")
                .comment("With 3.0 multiplier: 1 in 2730")
                .defineInRange("shiny_charm_multiplier", 3.0, 1.0, 100.0);
        SHINY_CHARM_DEX_THRESHOLD = BUILDER
                .comment("Percentage of the national Pokédex that must be caught to receive the Shiny Charm")
                .comment("Default: 100.0 (full national dex completion)")
                .comment("Example: 50.0 means the charm is given at 50% completion")
                .defineInRange("shiny_charm_dex_threshold", 100.0, 1.0, 100.0);
        BUILDER.pop();

        BUILDER.push("EXP Charm");
        ENABLE_EXP_CHARM = BUILDER
                .comment("Enable EXP Charm item")
                .define("enable_exp_charm", true);
        GRANT_EXP_CHARM_ON_ADVANCEMENT = BUILDER
                .comment("If true, the EXP Charm will be given as an item reward when its advancement is earned")
                .comment("Has no effect if grant_charm_on_advancement is false")
                .define("grant_exp_charm_on_advancement", true);
        EXP_CHARM_MULTIPLIER = BUILDER
                .comment("Experience multiplier when EXP Charm is equipped (default: 1.5 = 50% more EXP)")
                .defineInRange("exp_charm_multiplier", 1.5, 1.0, 10.0);
        BUILDER.pop();

        BUILDER.push("Catch Charm");
        ENABLE_CATCH_CHARM = BUILDER
                .comment("Enable Catch Charm item")
                .define("enable_catch_charm", true);
        GRANT_CATCH_CHARM_ON_ADVANCEMENT = BUILDER
                .comment("If true, the Catch Charm will be given as an item reward when its advancement is earned")
                .comment("Has no effect if grant_charm_on_advancement is false")
                .define("grant_catch_charm_on_advancement", true);
        CATCH_CHARM_MULTIPLIER = BUILDER
                .comment("Catch rate multiplier when Catch Charm is equipped (default: 2.0 = double catch rate)")
                .defineInRange("catch_charm_multiplier", 2.0, 1.0, 10.0);
        BUILDER.pop();

        BUILDER.push("Multi Charm");
        ENABLE_MULTI_CHARM = BUILDER
                .comment("Enable Multi Charm item (can combine multiple type charms)")
                .define("enable_multi_charm", true);
        BUILDER.pop();

        BUILDER.push("Type Charms");
        TYPE_CHARM_RADIUS = BUILDER
                .comment("Radius in blocks where type charms affect spawns (default: 64.0)")
                .defineInRange("type_charm_radius", 64.0, 1.0, 256.0);
        TYPE_CHARM_MATCH_MULTIPLIER = BUILDER
                .comment("Weight multiplier for Pokémon that match the charm type (default: 5.0)")
                .defineInRange("type_charm_match_multiplier", 5.0, 1.0, 100.0);
        TYPE_CHARM_NON_MATCH_MULTIPLIER = BUILDER
                .comment("Weight multiplier for Pokémon that don't match the charm type (default: 0.5)")
                .comment("Set to 1.0 to disable the penalty")
                .defineInRange("type_charm_non_match_multiplier", 0.5, 0.0, 1.0);
        TYPE_CHARM_THRESHOLD_PERCENTAGE = BUILDER
                .comment("Percentage of implemented species of a type that must be caught to receive that Type Charm")
                .comment("Calculated against currently implemented species, so it scales automatically as Cobblemon adds more")
                .comment("Evolutions count separately (Charmander, Charmeleon, Charizard = 3 Fire entries, 1 Flying entry)")
                .comment("Example: 10.0 means ~10 Fire (105 implemented), ~18 Water (182 implemented), ~7 Ice (70 implemented)")
                .defineInRange("type_charm_threshold_percentage", 80.0, 0.1, 100.0);

        BUILDER.push("Enable");
        ENABLE_BUG_CHARM      = BUILDER.define("enable_bug_charm", true);
        ENABLE_DARK_CHARM     = BUILDER.define("enable_dark_charm", true);
        ENABLE_DRAGON_CHARM   = BUILDER.define("enable_dragon_charm", true);
        ENABLE_ELECTRIC_CHARM = BUILDER.define("enable_electric_charm", true);
        ENABLE_FAIRY_CHARM    = BUILDER.define("enable_fairy_charm", true);
        ENABLE_FIGHTING_CHARM = BUILDER.define("enable_fighting_charm", true);
        ENABLE_FIRE_CHARM     = BUILDER.define("enable_fire_charm", true);
        ENABLE_FLYING_CHARM   = BUILDER.define("enable_flying_charm", true);
        ENABLE_GHOST_CHARM    = BUILDER.define("enable_ghost_charm", true);
        ENABLE_GRASS_CHARM    = BUILDER.define("enable_grass_charm", true);
        ENABLE_GROUND_CHARM   = BUILDER.define("enable_ground_charm", true);
        ENABLE_ICE_CHARM      = BUILDER.define("enable_ice_charm", true);
        ENABLE_NORMAL_CHARM   = BUILDER.define("enable_normal_charm", true);
        ENABLE_POISON_CHARM   = BUILDER.define("enable_poison_charm", true);
        ENABLE_PSYCHIC_CHARM  = BUILDER.define("enable_psychic_charm", true);
        ENABLE_ROCK_CHARM     = BUILDER.define("enable_rock_charm", true);
        ENABLE_STEEL_CHARM    = BUILDER.define("enable_steel_charm", true);
        ENABLE_WATER_CHARM    = BUILDER.define("enable_water_charm", true);
        BUILDER.pop();

        BUILDER.push("Grant On Advancement");
        GRANT_BUG_CHARM_ON_ADVANCEMENT      = BUILDER.define("grant_bug_charm_on_advancement", true);
        GRANT_DARK_CHARM_ON_ADVANCEMENT     = BUILDER.define("grant_dark_charm_on_advancement", true);
        GRANT_DRAGON_CHARM_ON_ADVANCEMENT   = BUILDER.define("grant_dragon_charm_on_advancement", true);
        GRANT_ELECTRIC_CHARM_ON_ADVANCEMENT = BUILDER.define("grant_electric_charm_on_advancement", true);
        GRANT_FAIRY_CHARM_ON_ADVANCEMENT    = BUILDER.define("grant_fairy_charm_on_advancement", true);
        GRANT_FIGHTING_CHARM_ON_ADVANCEMENT = BUILDER.define("grant_fighting_charm_on_advancement", true);
        GRANT_FIRE_CHARM_ON_ADVANCEMENT     = BUILDER.define("grant_fire_charm_on_advancement", true);
        GRANT_FLYING_CHARM_ON_ADVANCEMENT   = BUILDER.define("grant_flying_charm_on_advancement", true);
        GRANT_GHOST_CHARM_ON_ADVANCEMENT    = BUILDER.define("grant_ghost_charm_on_advancement", true);
        GRANT_GRASS_CHARM_ON_ADVANCEMENT    = BUILDER.define("grant_grass_charm_on_advancement", true);
        GRANT_GROUND_CHARM_ON_ADVANCEMENT   = BUILDER.define("grant_ground_charm_on_advancement", true);
        GRANT_ICE_CHARM_ON_ADVANCEMENT      = BUILDER.define("grant_ice_charm_on_advancement", true);
        GRANT_NORMAL_CHARM_ON_ADVANCEMENT   = BUILDER.define("grant_normal_charm_on_advancement", true);
        GRANT_POISON_CHARM_ON_ADVANCEMENT   = BUILDER.define("grant_poison_charm_on_advancement", true);
        GRANT_PSYCHIC_CHARM_ON_ADVANCEMENT  = BUILDER.define("grant_psychic_charm_on_advancement", true);
        GRANT_ROCK_CHARM_ON_ADVANCEMENT     = BUILDER.define("grant_rock_charm_on_advancement", true);
        GRANT_STEEL_CHARM_ON_ADVANCEMENT    = BUILDER.define("grant_steel_charm_on_advancement", true);
        GRANT_WATER_CHARM_ON_ADVANCEMENT    = BUILDER.define("grant_water_charm_on_advancement", true);
        BUILDER.pop();

        TYPE_CHARM_CONFIG_MAP.put(CharmType.NORMAL,   ENABLE_NORMAL_CHARM);
        TYPE_CHARM_CONFIG_MAP.put(CharmType.FIRE,     ENABLE_FIRE_CHARM);
        TYPE_CHARM_CONFIG_MAP.put(CharmType.WATER,    ENABLE_WATER_CHARM);
        TYPE_CHARM_CONFIG_MAP.put(CharmType.ELECTRIC, ENABLE_ELECTRIC_CHARM);
        TYPE_CHARM_CONFIG_MAP.put(CharmType.GRASS,    ENABLE_GRASS_CHARM);
        TYPE_CHARM_CONFIG_MAP.put(CharmType.ICE,      ENABLE_ICE_CHARM);
        TYPE_CHARM_CONFIG_MAP.put(CharmType.FIGHTING, ENABLE_FIGHTING_CHARM);
        TYPE_CHARM_CONFIG_MAP.put(CharmType.POISON,   ENABLE_POISON_CHARM);
        TYPE_CHARM_CONFIG_MAP.put(CharmType.GROUND,   ENABLE_GROUND_CHARM);
        TYPE_CHARM_CONFIG_MAP.put(CharmType.FLYING,   ENABLE_FLYING_CHARM);
        TYPE_CHARM_CONFIG_MAP.put(CharmType.PSYCHIC,  ENABLE_PSYCHIC_CHARM);
        TYPE_CHARM_CONFIG_MAP.put(CharmType.BUG,      ENABLE_BUG_CHARM);
        TYPE_CHARM_CONFIG_MAP.put(CharmType.ROCK,     ENABLE_ROCK_CHARM);
        TYPE_CHARM_CONFIG_MAP.put(CharmType.GHOST,    ENABLE_GHOST_CHARM);
        TYPE_CHARM_CONFIG_MAP.put(CharmType.DRAGON,   ENABLE_DRAGON_CHARM);
        TYPE_CHARM_CONFIG_MAP.put(CharmType.DARK,     ENABLE_DARK_CHARM);
        TYPE_CHARM_CONFIG_MAP.put(CharmType.STEEL,    ENABLE_STEEL_CHARM);
        TYPE_CHARM_CONFIG_MAP.put(CharmType.FAIRY,    ENABLE_FAIRY_CHARM);

        TYPE_CHARM_GRANT_MAP.put(CharmType.NORMAL,   GRANT_NORMAL_CHARM_ON_ADVANCEMENT);
        TYPE_CHARM_GRANT_MAP.put(CharmType.FIRE,     GRANT_FIRE_CHARM_ON_ADVANCEMENT);
        TYPE_CHARM_GRANT_MAP.put(CharmType.WATER,    GRANT_WATER_CHARM_ON_ADVANCEMENT);
        TYPE_CHARM_GRANT_MAP.put(CharmType.ELECTRIC, GRANT_ELECTRIC_CHARM_ON_ADVANCEMENT);
        TYPE_CHARM_GRANT_MAP.put(CharmType.GRASS,    GRANT_GRASS_CHARM_ON_ADVANCEMENT);
        TYPE_CHARM_GRANT_MAP.put(CharmType.ICE,      GRANT_ICE_CHARM_ON_ADVANCEMENT);
        TYPE_CHARM_GRANT_MAP.put(CharmType.FIGHTING, GRANT_FIGHTING_CHARM_ON_ADVANCEMENT);
        TYPE_CHARM_GRANT_MAP.put(CharmType.POISON,   GRANT_POISON_CHARM_ON_ADVANCEMENT);
        TYPE_CHARM_GRANT_MAP.put(CharmType.GROUND,   GRANT_GROUND_CHARM_ON_ADVANCEMENT);
        TYPE_CHARM_GRANT_MAP.put(CharmType.FLYING,   GRANT_FLYING_CHARM_ON_ADVANCEMENT);
        TYPE_CHARM_GRANT_MAP.put(CharmType.PSYCHIC,  GRANT_PSYCHIC_CHARM_ON_ADVANCEMENT);
        TYPE_CHARM_GRANT_MAP.put(CharmType.BUG,      GRANT_BUG_CHARM_ON_ADVANCEMENT);
        TYPE_CHARM_GRANT_MAP.put(CharmType.ROCK,     GRANT_ROCK_CHARM_ON_ADVANCEMENT);
        TYPE_CHARM_GRANT_MAP.put(CharmType.GHOST,    GRANT_GHOST_CHARM_ON_ADVANCEMENT);
        TYPE_CHARM_GRANT_MAP.put(CharmType.DRAGON,   GRANT_DRAGON_CHARM_ON_ADVANCEMENT);
        TYPE_CHARM_GRANT_MAP.put(CharmType.DARK,     GRANT_DARK_CHARM_ON_ADVANCEMENT);
        TYPE_CHARM_GRANT_MAP.put(CharmType.STEEL,    GRANT_STEEL_CHARM_ON_ADVANCEMENT);
        TYPE_CHARM_GRANT_MAP.put(CharmType.FAIRY,    GRANT_FAIRY_CHARM_ON_ADVANCEMENT);

        BUILDER.pop(); // Type Charms
        BUILDER.pop(); // Charms

        SPEC = BUILDER.build();
    }

    /**
     * Returns true if both the global type charm master toggle and the per-type toggle are enabled.
     */
    public static boolean isTypeCharmEnabled(CharmType type) {
        if (!ENABLE_ALL_TYPE_CHARMS.get()) return false;
        ModConfigSpec.BooleanValue value = TYPE_CHARM_CONFIG_MAP.get(type);
        return value == null || value.get();
    }

    /**
     * Returns true if the global grant toggle and the per-type grant toggle are both enabled.
     */
    public static boolean isTypeCharmGrantedOnAdvancement(CharmType type) {
        if (!GRANT_CHARM_ON_ADVANCEMENT.get()) return false;
        ModConfigSpec.BooleanValue value = TYPE_CHARM_GRANT_MAP.get(type);
        return value == null || value.get();
    }

    /**
     * Called client-side when a SyncConfigPayload is received from the server.
     * Overwrites in-memory values with server values so tooltips reflect server config.
     */
    public static void syncFromServer(
            boolean charmEffectRequiresAdvancement,
            boolean grantCharmOnAdvancement,
            float shinyCharmMultiplier,
            float expCharmMultiplier,
            float catchCharmMultiplier,
            float typeCharmMatchMultiplier,
            float typeCharmNonMatchMultiplier,
            double typeCharmRadius,
            double typeCharmThresholdPercentage
    ) {
        CHARM_EFFECT_REQUIRES_ADVANCEMENT.set(charmEffectRequiresAdvancement);
        GRANT_CHARM_ON_ADVANCEMENT.set(grantCharmOnAdvancement);
        SHINY_CHARM_MULTIPLIER.set((double) shinyCharmMultiplier);
        EXP_CHARM_MULTIPLIER.set((double) expCharmMultiplier);
        CATCH_CHARM_MULTIPLIER.set((double) catchCharmMultiplier);
        TYPE_CHARM_MATCH_MULTIPLIER.set((double) typeCharmMatchMultiplier);
        TYPE_CHARM_NON_MATCH_MULTIPLIER.set((double) typeCharmNonMatchMultiplier);
        TYPE_CHARM_RADIUS.set(typeCharmRadius);
        TYPE_CHARM_THRESHOLD_PERCENTAGE.set(typeCharmThresholdPercentage);
    }
}