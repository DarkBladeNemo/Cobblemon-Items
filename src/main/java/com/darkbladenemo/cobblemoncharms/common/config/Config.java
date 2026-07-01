package com.darkbladenemo.cobblemoncharms.common.config;

import com.darkbladenemo.cobblemoncharms.common.item.charm.CharmType;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

public class Config {

    // All registered values — used for load/save

    private static final java.util.List<BooleanValue> ALL_BOOLS   = new java.util.ArrayList<>();
    private static final java.util.List<DoubleValue>  ALL_DOUBLES = new java.util.ArrayList<>();
    private static final java.util.List<IntValue>     ALL_INTS    = new java.util.ArrayList<>();

    // Value wrappers — same API as NeoForge's BooleanValue / DoubleValue / IntValue

    public static class BooleanValue {
        private final String key;
        private final boolean defaultValue;
        private boolean value;

        BooleanValue(String key, boolean defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.value = defaultValue;
        }

        public boolean get() { return value; }
        void set(boolean v) { this.value = v; }
        String getKey() { return key; }
        boolean getDefault() { return defaultValue; }
    }

    public static class DoubleValue {
        private final String key;
        private final double defaultValue;
        private final double min, max;
        private double value;

        DoubleValue(String key, double defaultValue, double min, double max) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.min = min;
            this.max = max;
            this.value = defaultValue;
        }

        public double get() { return value; }
        public float floatValue() { return (float) value; }
        void set(double v) { this.value = Math.max(min, Math.min(max, v)); }
        String getKey() { return key; }
        double getDefault() { return defaultValue; }
    }

    public static class IntValue {
        private final String key;
        private final int defaultValue;
        private final int min, max;
        private int value;

        IntValue(String key, int defaultValue, int min, int max) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.min = min;
            this.max = max;
            this.value = defaultValue;
        }

        public Integer get() { return value; }
        void set(int v) { this.value = Math.max(min, Math.min(max, v)); }
        String getKey() { return key; }
        int getDefault() { return defaultValue; }
    }

    // SPEC — used by existing code to guard against config not being loaded yet

    public static final Spec SPEC = new Spec();

    public static class Spec {
        private boolean loaded = false;
        public boolean isLoaded() { return loaded; }
        void setLoaded() { loaded = true; }
    }

    // Config values — identical names to the NeoForge version

    // Quick toggles
    public static final BooleanValue ENABLE_ALL_EV_ITEMS    = bool("enable_all_ev_items",    false);
    public static final BooleanValue ENABLE_ALL_IV_ITEMS    = bool("enable_all_iv_items",    false);
    public static final BooleanValue ENABLE_ALL_TYPE_CHARMS = bool("enable_all_type_charms", true);

    // Global charm settings
    public static final BooleanValue CHARM_EFFECT_REQUIRES_ADVANCEMENT = bool("charm_effect_requires_advancement", true);
    public static final BooleanValue GRANT_CHARM_ON_ADVANCEMENT = bool("grant_charm_on_advancement",       true);

    // Shiny Charm
    public static final BooleanValue ENABLE_SHINY_CHARM       = bool("enable_shiny_charm", true);
    public static final DoubleValue  SHINY_CHARM_MULTIPLIER    = doubleVal("shiny_charm_multiplier",    3.0,   1.0, 100.0);
    public static final DoubleValue  SHINY_CHARM_DEX_THRESHOLD = doubleVal("shiny_charm_dex_threshold", 100.0, 1.0, 100.0);

    // EXP Charm
    public static final BooleanValue ENABLE_EXP_CHARM      = bool("enable_exp_charm", true);
    public static final DoubleValue  EXP_CHARM_MULTIPLIER   = doubleVal("exp_charm_multiplier", 1.5, 1.0, 10.0);

    // Catch Charm
    public static final BooleanValue ENABLE_CATCH_CHARM      = bool("enable_catch_charm", true);
    public static final DoubleValue  CATCH_CHARM_MULTIPLIER  = doubleVal("catch_charm_multiplier", 1.5, 1.0, 10.0);

    // Multi Charm
    public static final BooleanValue ENABLE_MULTI_CHARM = bool("enable_multi_charm", true);

    // Type Charm global settings
    public static final DoubleValue TYPE_CHARM_RADIUS               = doubleVal("type_charm_radius",               64.0, 1.0,  256.0);
    public static final DoubleValue TYPE_CHARM_MATCH_MULTIPLIER     = doubleVal("type_charm_match_multiplier",     5.0,  1.0,  100.0);
    public static final DoubleValue TYPE_CHARM_NON_MATCH_MULTIPLIER = doubleVal("type_charm_non_match_multiplier", 0.5,  0.0,  1.0);
    public static final DoubleValue TYPE_CHARM_THRESHOLD_PERCENTAGE = doubleVal("type_charm_threshold_percentage", 80.0, 0.1,  100.0);

    // Individual type charm toggles
    public static final BooleanValue ENABLE_BUG_CHARM      = bool("enable_bug_charm",      true);
    public static final BooleanValue ENABLE_DARK_CHARM     = bool("enable_dark_charm",     true);
    public static final BooleanValue ENABLE_DRAGON_CHARM   = bool("enable_dragon_charm",   true);
    public static final BooleanValue ENABLE_ELECTRIC_CHARM = bool("enable_electric_charm", true);
    public static final BooleanValue ENABLE_FAIRY_CHARM    = bool("enable_fairy_charm",    true);
    public static final BooleanValue ENABLE_FIGHTING_CHARM = bool("enable_fighting_charm", true);
    public static final BooleanValue ENABLE_FIRE_CHARM     = bool("enable_fire_charm",     true);
    public static final BooleanValue ENABLE_FLYING_CHARM   = bool("enable_flying_charm",   true);
    public static final BooleanValue ENABLE_GHOST_CHARM    = bool("enable_ghost_charm",    true);
    public static final BooleanValue ENABLE_GRASS_CHARM    = bool("enable_grass_charm",    true);
    public static final BooleanValue ENABLE_GROUND_CHARM   = bool("enable_ground_charm",   true);
    public static final BooleanValue ENABLE_ICE_CHARM      = bool("enable_ice_charm",      true);
    public static final BooleanValue ENABLE_NORMAL_CHARM   = bool("enable_normal_charm",   true);
    public static final BooleanValue ENABLE_POISON_CHARM   = bool("enable_poison_charm",   true);
    public static final BooleanValue ENABLE_PSYCHIC_CHARM  = bool("enable_psychic_charm",  true);
    public static final BooleanValue ENABLE_ROCK_CHARM     = bool("enable_rock_charm",     true);
    public static final BooleanValue ENABLE_STEEL_CHARM    = bool("enable_steel_charm",    true);
    public static final BooleanValue ENABLE_WATER_CHARM    = bool("enable_water_charm",    true);

    // EV amounts
    public static final IntValue SUPER_EV_INCREASE_AMOUNT = intVal("super_ev_increase_amount", 100, 1, 252);

    // Individual EV item toggles
    public static final BooleanValue ENABLE_SUPER_CARBOS  = bool("enable_super_carbos",  true);
    public static final BooleanValue ENABLE_SUPER_PROTEIN = bool("enable_super_protein", true);
    public static final BooleanValue ENABLE_SUPER_HP_UP   = bool("enable_super_hp_up",   true);
    public static final BooleanValue ENABLE_SUPER_IRON    = bool("enable_super_iron",    true);
    public static final BooleanValue ENABLE_SUPER_CALCIUM = bool("enable_super_calcium", true);
    public static final BooleanValue ENABLE_SUPER_ZINC    = bool("enable_super_zinc",    true);

    // IV amounts
    public static final IntValue SUPER_IV_INCREASE_AMOUNT = intVal("super_iv_increase_amount", 10, 1,  31);
    public static final IntValue GOLD_BOTTLE_CAP_IV_AMOUNT = intVal("gold_bottle_cap_iv_amount", 31, 1, 31);

    // Individual IV item toggles
    public static final BooleanValue ENABLE_SUPER_HEALTH_CANDY  = bool("enable_super_health_candy",  true);
    public static final BooleanValue ENABLE_SUPER_MIGHTY_CANDY  = bool("enable_super_mighty_candy",  true);
    public static final BooleanValue ENABLE_SUPER_TOUGH_CANDY   = bool("enable_super_tough_candy",   true);
    public static final BooleanValue ENABLE_SUPER_SMART_CANDY   = bool("enable_super_smart_candy",   true);
    public static final BooleanValue ENABLE_SUPER_COURAGE_CANDY = bool("enable_super_courage_candy", true);
    public static final BooleanValue ENABLE_SUPER_QUICK_CANDY   = bool("enable_super_quick_candy",   true);
    public static final BooleanValue ENABLE_GOLD_BOTTLE_CAP     = bool("enable_gold_bottle_cap",     true);

    // Type charm lookup map — same as NeoForge version

    private static final Map<CharmType, BooleanValue> TYPE_CHARM_CONFIG_MAP = new EnumMap<>(CharmType.class);

    static {
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
    }

    public static boolean isTypeCharmEnabled(CharmType type) {
        if (!ENABLE_ALL_TYPE_CHARMS.get()) return false;
        BooleanValue value = TYPE_CHARM_CONFIG_MAP.get(type);
        return value == null || value.get();
    }

    // Init — call from CobblemonCharmsFabric.onInitialize()

    public static void init() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("cobblemoncharms.json");
        load(configPath);
        save(configPath); // write back to create file / fill in missing keys
        SPEC.setLoaded();
    }

    // Load / save

    private static void load(Path path) {
        File file = path.toFile();
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            for (BooleanValue v : ALL_BOOLS) {
                if (root.has(v.getKey())) {
                    v.set(root.get(v.getKey()).getAsBoolean());
                }
            }
            for (DoubleValue v : ALL_DOUBLES) {
                if (root.has(v.getKey())) {
                    v.set(root.get(v.getKey()).getAsDouble());
                }
            }
            for (IntValue v : ALL_INTS) {
                if (root.has(v.getKey())) {
                    v.set(root.get(v.getKey()).getAsInt());
                }
            }
        } catch (Exception e) {
            System.err.println("[CobblemonCharms] Failed to load config: " + e.getMessage());
        }
    }

    private static void save(Path path) {
        JsonObject root = new JsonObject();

        for (BooleanValue v : ALL_BOOLS)   root.addProperty(v.getKey(), v.get());
        for (DoubleValue  v : ALL_DOUBLES)  root.addProperty(v.getKey(), v.get());
        for (IntValue     v : ALL_INTS)     root.addProperty(v.getKey(), v.get());

        try (Writer writer = new FileWriter(path.toFile())) {
            new GsonBuilder().setPrettyPrinting().create().toJson(root, writer);
        } catch (Exception e) {
            System.err.println("[CobblemonCharms] Failed to save config: " + e.getMessage());
        }
    }

    // Factory helpers — register and return value wrappers

    private static BooleanValue bool(String key, boolean def) {
        BooleanValue v = new BooleanValue(key, def);
        ALL_BOOLS.add(v);
        return v;
    }

    private static DoubleValue doubleVal(String key, double def, double min, double max) {
        DoubleValue v = new DoubleValue(key, def, min, max);
        ALL_DOUBLES.add(v);
        return v;
    }

    private static IntValue intVal(String key, int def, int min, int max) {
        IntValue v = new IntValue(key, def, min, max);
        ALL_INTS.add(v);
        return v;
    }
}