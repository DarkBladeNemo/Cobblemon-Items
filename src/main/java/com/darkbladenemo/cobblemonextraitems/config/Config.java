package com.darkbladenemo.cobblemonextraitems.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    // Only IV amount is configurable (EV amount is hardcoded to 100)
    public static final ModConfigSpec.IntValue HIGH_IV_INCREASE_AMOUNT;

    // Enable/Disable items (optional - can remove if you always want all items)
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_CARBOS;
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_PROTEIN;
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_HP_UP;
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_IRON;
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_CALCIUM;
    public static final ModConfigSpec.BooleanValue ENABLE_HIGH_ZINC;
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

        ENABLE_HIGH_HEALTH_CANDY = BUILDER.define("enable_high_health_candy", true);
        ENABLE_HIGH_MIGHTY_CANDY = BUILDER.define("enable_high_mighty_candy", true);
        ENABLE_HIGH_TOUGH_CANDY = BUILDER.define("enable_high_tough_candy", true);
        ENABLE_HIGH_SMART_CANDY = BUILDER.define("enable_high_smart_candy", true);
        ENABLE_HIGH_COURAGE_CANDY = BUILDER.define("enable_high_courage_candy", true);
        ENABLE_HIGH_QUICK_CANDY = BUILDER.define("enable_high_quick_candy", true);

        BUILDER.pop();

        BUILDER.push("High EV Items Configuration");
        ENABLE_HIGH_CARBOS = BUILDER.define("enable_high_carbos", true);
        ENABLE_HIGH_PROTEIN = BUILDER.define("enable_high_protein", true);
        ENABLE_HIGH_HP_UP = BUILDER.define("enable_high_hp_up", true);
        ENABLE_HIGH_IRON = BUILDER.define("enable_high_iron", true);
        ENABLE_HIGH_CALCIUM = BUILDER.define("enable_high_calcium", true);
        ENABLE_HIGH_ZINC = BUILDER.define("enable_high_zinc", true);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}