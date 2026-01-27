package com.darkbladenemo.cobblemonextraitems.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    // Charm Configuration
    public static final ModConfigSpec.BooleanValue ENABLE_SHINY_CHARM;
    public static final ModConfigSpec.DoubleValue SHINY_CHARM_MULTIPLIER;
    public static final ModConfigSpec.BooleanValue ENABLE_EXP_CHARM;
    public static final ModConfigSpec.DoubleValue EXP_CHARM_MULTIPLIER;
    public static final ModConfigSpec.BooleanValue ENABLE_MULTI_CHARM;

    // Type Charm Configuration
    public static final ModConfigSpec.DoubleValue TYPE_CHARM_RADIUS;
    public static final ModConfigSpec.DoubleValue TYPE_CHARM_MULTIPLIER;

    // Enable/Disable type charms
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

    // EV and IV increase amounts
    public static final ModConfigSpec.IntValue HIGH_IV_INCREASE_AMOUNT;
    public static final ModConfigSpec.IntValue GOLD_BOTTLE_CAP_IV_AMOUNT;
    public static final ModConfigSpec.IntValue HIGH_EV_INCREASE_AMOUNT;

    // Enable/Disable EV items
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_CARBOS;
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_PROTEIN;
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_HP_UP;
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_IRON;
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_CALCIUM;
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_ZINC;

    // Enable/Disable IV items
    public static final ModConfigSpec.BooleanValue ENABLE_GOLD_BOTTLE_CAP;
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_HEALTH_CANDY;
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_MIGHTY_CANDY;
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_TOUGH_CANDY;
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_SMART_CANDY;
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_COURAGE_CANDY;
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_QUICK_CANDY;

    static {
        BUILDER.push("High IV Items Configuration");

        HIGH_IV_INCREASE_AMOUNT = BUILDER
                .comment("Amount of IVs added by high IV items (default: 10, range: 1-31)")
                .defineInRange("high_iv_increase_amount", 10, 1, 31);
        GOLD_BOTTLE_CAP_IV_AMOUNT = BUILDER
                .comment("Amount of IVs added by Gold Bottle Cap to ALL stats (default: 31 for max IVs, range: 1-31)")
                .defineInRange("gold_bottle_cap_iv_amount", 31, 1, 31);

        ENABLE_HIGH_HEALTH_CANDY = BUILDER.define("enable_high_health_candy", true);
        ENABLE_HIGH_MIGHTY_CANDY = BUILDER.define("enable_high_mighty_candy", true);
        ENABLE_HIGH_TOUGH_CANDY = BUILDER.define("enable_high_tough_candy", true);
        ENABLE_HIGH_SMART_CANDY = BUILDER.define("enable_high_smart_candy", true);
        ENABLE_HIGH_COURAGE_CANDY = BUILDER.define("enable_high_courage_candy", true);
        ENABLE_HIGH_QUICK_CANDY = BUILDER.define("enable_high_quick_candy", true);
        ENABLE_GOLD_BOTTLE_CAP = BUILDER
                .comment("Enable Gold Bottle Cap (increases all IVs)")
                .define("enable_gold_bottle_cap", true);

        BUILDER.pop();

        BUILDER.push("High EV Items Configuration");

        HIGH_EV_INCREASE_AMOUNT = BUILDER
                .comment("Amount of EVs added by high EV items (default: 100, range: 1-252)")
                .defineInRange("high_ev_increase_amount", 100, 1, 252);

        ENABLE_HIGH_CARBOS = BUILDER.define("enable_high_carbos", true);
        ENABLE_HIGH_PROTEIN = BUILDER.define("enable_high_protein", true);
        ENABLE_HIGH_HP_UP = BUILDER.define("enable_high_hp_up", true);
        ENABLE_HIGH_IRON = BUILDER.define("enable_high_iron", true);
        ENABLE_HIGH_CALCIUM = BUILDER.define("enable_high_calcium", true);
        ENABLE_HIGH_ZINC = BUILDER.define("enable_high_zinc", true);
        BUILDER.pop();

        BUILDER.push("Charm Configuration");
        SHINY_CHARM_MULTIPLIER = BUILDER
                .comment("Shiny Charm multiplier (default: 3.0 = 3x better shiny odds)")
                .comment("Cobblemon base rate: 1 in 8192")
                .comment("With 3.0 multiplier: 1 in 2730")
                .defineInRange("shiny_charm_multiplier", 3.0, 1.0, 100.0);

        ENABLE_SHINY_CHARM = BUILDER
                .comment("Enable Shiny Charm item")
                .define("enable_shiny_charm", true);

        BUILDER.pop();

        BUILDER.push("Type Charm Configuration");
        TYPE_CHARM_RADIUS = BUILDER
                .comment("Radius in blocks where type charms affect spawns (default: 32.0)")
                .defineInRange("type_charm_radius", 32.0, 1.0, 256.0);

        TYPE_CHARM_MULTIPLIER = BUILDER
                .comment("Weight multiplier per charm (default: 2.0 = doubles spawn chance)")
                .comment("With 2 Fire Charms: 1 + (2.0-1)*2 = 3x spawn chance")
                .defineInRange("type_charm_multiplier", 2.0, 1.0, 100.0);

        ENABLE_BUG_CHARM = BUILDER.define("enable_bug_charm", true);
        ENABLE_DARK_CHARM = BUILDER.define("enable_dark_charm", true);
        ENABLE_DRAGON_CHARM = BUILDER.define("enable_dragon_charm", true);
        ENABLE_ELECTRIC_CHARM = BUILDER.define("enable_electric_charm", true);
        ENABLE_FAIRY_CHARM = BUILDER.define("enable_fairy_charm", true);
        ENABLE_FIGHTING_CHARM = BUILDER.define("enable_fighting_charm", true);
        ENABLE_FIRE_CHARM = BUILDER.define("enable_fire_charm", true);
        ENABLE_FLYING_CHARM = BUILDER.define("enable_flying_charm", true);
        ENABLE_GHOST_CHARM = BUILDER.define("enable_ghost_charm", true);
        ENABLE_GRASS_CHARM = BUILDER.define("enable_grass_charm", true);
        ENABLE_GROUND_CHARM = BUILDER.define("enable_ground_charm", true);
        ENABLE_ICE_CHARM = BUILDER.define("enable_ice_charm", true);
        ENABLE_NORMAL_CHARM = BUILDER.define("enable_normal_charm", true);
        ENABLE_POISON_CHARM = BUILDER.define("enable_poison_charm", true);
        ENABLE_PSYCHIC_CHARM = BUILDER.define("enable_psychic_charm", true);
        ENABLE_ROCK_CHARM = BUILDER.define("enable_rock_charm", true);
        ENABLE_STEEL_CHARM = BUILDER.define("enable_steel_charm", true);
        ENABLE_WATER_CHARM = BUILDER.define("enable_water_charm", true);

        ENABLE_MULTI_CHARM = BUILDER
                .comment("Enable Multi Charm item (can combine multiple type charms)")
                .define("enable_multi_charm", true);

        BUILDER.pop();

        BUILDER.push("Experience Charm Configuration");
        EXP_CHARM_MULTIPLIER = BUILDER
                .comment("Experience multiplier when EXP Charm is equipped (default: 1.5 = 50% more EXP)")
                .defineInRange("exp_charm_multiplier", 1.5, 1.0, 10.0);

        ENABLE_EXP_CHARM = BUILDER
                .comment("Enable EXP Charm item")
                .define("enable_exp_charm", true);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}