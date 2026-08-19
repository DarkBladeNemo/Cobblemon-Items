package com.darkbladenemo.cobblemoncharms.common.config;

import com.darkbladenemo.cobblemoncharms.common.item.charm.CharmType;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

public class Config {

    private static final java.util.List<BooleanValue> ALL_BOOLS   = new java.util.ArrayList<>();
    private static final java.util.List<DoubleValue>  ALL_DOUBLES = new java.util.ArrayList<>();
    private static final java.util.List<IntValue>     ALL_INTS    = new java.util.ArrayList<>();

    public static class BooleanValue {
        private final String[] path;
        private boolean value;

        BooleanValue(boolean defaultValue, String... path) {
            this.path = path;
            this.value = defaultValue;
        }

        public boolean get() { return value; }
        void set(boolean v) { this.value = v; }
        String[] getPath() { return path; }
    }

    public static class DoubleValue {
        private final String[] path;
        private final double min, max;
        private double value;

        DoubleValue(double defaultValue, double min, double max, String... path) {
            this.path = path;
            this.min = min;
            this.max = max;
            this.value = defaultValue;
        }

        public double get() { return value; }
        public float floatValue() { return (float) value; }
        void set(double v) { this.value = Math.clamp(v, min, max); }
        String[] getPath() { return path; }
    }

    public static class IntValue {
        private final String[] path;
        private final int min, max;
        private int value;

        IntValue(int defaultValue, int min, int max, String... path) {
            this.path = path;
            this.min = min;
            this.max = max;
            this.value = defaultValue;
        }

        public Integer get() { return value; }
        void set(int v) { this.value = Math.clamp(v, min, max); }
        String[] getPath() { return path; }
    }

    public static final Spec SPEC = new Spec();

    public static class Spec {
        private boolean loaded = false;
        public boolean isLoaded() { return loaded; }
        void setLoaded() { loaded = true; }
    }

    // Quick Toggles
    public static final BooleanValue ENABLE_ALL_EV_ITEMS    = bool(false, "quick_toggles", "enable_all_ev_items");
    public static final BooleanValue ENABLE_ALL_IV_ITEMS    = bool(false, "quick_toggles", "enable_all_iv_items");
    public static final BooleanValue ENABLE_ALL_TYPE_CHARMS = bool(true,  "quick_toggles", "enable_all_type_charms");
    public static final BooleanValue GRANT_CHARM_ON_ADVANCEMENT = bool(true, "quick_toggles", "grant_charm_on_advancement");

    // Global Charm Settings
    public static final BooleanValue CHARM_EFFECT_REQUIRES_ADVANCEMENT =
            bool(true, "charms", "charm_effect_requires_advancement");

    // Shiny Charm
    public static final BooleanValue ENABLE_SHINY_CHARM =
            bool(true, "charms", "shiny_charm", "enabled");
    public static final BooleanValue SHINY_CHARM_AFFECTS_HONEY_LOG =
            bool(true, "charms", "shiny_charm", "affects_honey_log");
    public static final DoubleValue  SHINY_CHARM_MULTIPLIER =
            doubleVal(3.0, 1.0, 100.0, "charms", "shiny_charm", "multiplier");
    public static final DoubleValue  SHINY_CHARM_DEX_THRESHOLD =
            doubleVal(100.0, 1.0, 100.0, "charms", "shiny_charm", "dex_threshold");
    public static final BooleanValue GRANT_SHINY_CHARM_ON_ADVANCEMENT =
            bool(true, "charms", "shiny_charm", "grant_on_advancement");

    // EXP Charm
    public static final BooleanValue ENABLE_EXP_CHARM =
            bool(true, "charms", "exp_charm", "enabled");
    public static final DoubleValue  EXP_CHARM_MULTIPLIER =
            doubleVal(1.5, 1.0, 10.0, "charms", "exp_charm", "multiplier");
    public static final BooleanValue GRANT_EXP_CHARM_ON_ADVANCEMENT =
            bool(true, "charms", "exp_charm", "grant_on_advancement");

    // Catch Charm
    public static final BooleanValue ENABLE_CATCH_CHARM =
            bool(true, "charms", "catch_charm", "enabled");
    public static final DoubleValue  CATCH_CHARM_MULTIPLIER =
            doubleVal(2.0, 1.0, 10.0, "charms", "catch_charm", "multiplier");
    public static final BooleanValue GRANT_CATCH_CHARM_ON_ADVANCEMENT =
            bool(true, "charms", "catch_charm", "grant_on_advancement");

    // Multi Charm
    public static final BooleanValue ENABLE_MULTI_CHARM =
            bool(true, "charms", "multi_charm", "enabled");

    // Type Charms — global settings
    public static final DoubleValue TYPE_CHARM_RADIUS =
            doubleVal(64.0, 1.0, 256.0, "charms", "type_charms", "radius");
    public static final DoubleValue TYPE_CHARM_MATCH_MULTIPLIER =
            doubleVal(5.0, 1.0, 100.0, "charms", "type_charms", "match_multiplier");
    public static final DoubleValue TYPE_CHARM_NON_MATCH_MULTIPLIER =
            doubleVal(0.5, 0.0, 1.0, "charms", "type_charms", "non_match_multiplier");
    public static final DoubleValue TYPE_CHARM_THRESHOLD_PERCENTAGE =
            doubleVal(80.0, 0.1, 100.0, "charms", "type_charms", "threshold_percentage");

    // Type Charms — per-type enable toggles
    public static final BooleanValue ENABLE_BUG_CHARM      = bool(true, "charms", "type_charms", "enable", "bug");
    public static final BooleanValue ENABLE_DARK_CHARM     = bool(true, "charms", "type_charms", "enable", "dark");
    public static final BooleanValue ENABLE_DRAGON_CHARM   = bool(true, "charms", "type_charms", "enable", "dragon");
    public static final BooleanValue ENABLE_ELECTRIC_CHARM = bool(true, "charms", "type_charms", "enable", "electric");
    public static final BooleanValue ENABLE_FAIRY_CHARM    = bool(true, "charms", "type_charms", "enable", "fairy");
    public static final BooleanValue ENABLE_FIGHTING_CHARM = bool(true, "charms", "type_charms", "enable", "fighting");
    public static final BooleanValue ENABLE_FIRE_CHARM     = bool(true, "charms", "type_charms", "enable", "fire");
    public static final BooleanValue ENABLE_FLYING_CHARM   = bool(true, "charms", "type_charms", "enable", "flying");
    public static final BooleanValue ENABLE_GHOST_CHARM    = bool(true, "charms", "type_charms", "enable", "ghost");
    public static final BooleanValue ENABLE_GRASS_CHARM    = bool(true, "charms", "type_charms", "enable", "grass");
    public static final BooleanValue ENABLE_GROUND_CHARM   = bool(true, "charms", "type_charms", "enable", "ground");
    public static final BooleanValue ENABLE_ICE_CHARM      = bool(true, "charms", "type_charms", "enable", "ice");
    public static final BooleanValue ENABLE_NORMAL_CHARM   = bool(true, "charms", "type_charms", "enable", "normal");
    public static final BooleanValue ENABLE_POISON_CHARM   = bool(true, "charms", "type_charms", "enable", "poison");
    public static final BooleanValue ENABLE_PSYCHIC_CHARM  = bool(true, "charms", "type_charms", "enable", "psychic");
    public static final BooleanValue ENABLE_ROCK_CHARM     = bool(true, "charms", "type_charms", "enable", "rock");
    public static final BooleanValue ENABLE_STEEL_CHARM    = bool(true, "charms", "type_charms", "enable", "steel");
    public static final BooleanValue ENABLE_WATER_CHARM    = bool(true, "charms", "type_charms", "enable", "water");

    // Type Charms — per-type grant on advancement toggles
    public static final BooleanValue GRANT_BUG_CHARM_ON_ADVANCEMENT      = bool(true, "charms", "type_charms", "grant_on_advancement", "bug");
    public static final BooleanValue GRANT_DARK_CHARM_ON_ADVANCEMENT     = bool(true, "charms", "type_charms", "grant_on_advancement", "dark");
    public static final BooleanValue GRANT_DRAGON_CHARM_ON_ADVANCEMENT   = bool(true, "charms", "type_charms", "grant_on_advancement", "dragon");
    public static final BooleanValue GRANT_ELECTRIC_CHARM_ON_ADVANCEMENT = bool(true, "charms", "type_charms", "grant_on_advancement", "electric");
    public static final BooleanValue GRANT_FAIRY_CHARM_ON_ADVANCEMENT    = bool(true, "charms", "type_charms", "grant_on_advancement", "fairy");
    public static final BooleanValue GRANT_FIGHTING_CHARM_ON_ADVANCEMENT = bool(true, "charms", "type_charms", "grant_on_advancement", "fighting");
    public static final BooleanValue GRANT_FIRE_CHARM_ON_ADVANCEMENT     = bool(true, "charms", "type_charms", "grant_on_advancement", "fire");
    public static final BooleanValue GRANT_FLYING_CHARM_ON_ADVANCEMENT   = bool(true, "charms", "type_charms", "grant_on_advancement", "flying");
    public static final BooleanValue GRANT_GHOST_CHARM_ON_ADVANCEMENT    = bool(true, "charms", "type_charms", "grant_on_advancement", "ghost");
    public static final BooleanValue GRANT_GRASS_CHARM_ON_ADVANCEMENT    = bool(true, "charms", "type_charms", "grant_on_advancement", "grass");
    public static final BooleanValue GRANT_GROUND_CHARM_ON_ADVANCEMENT   = bool(true, "charms", "type_charms", "grant_on_advancement", "ground");
    public static final BooleanValue GRANT_ICE_CHARM_ON_ADVANCEMENT      = bool(true, "charms", "type_charms", "grant_on_advancement", "ice");
    public static final BooleanValue GRANT_NORMAL_CHARM_ON_ADVANCEMENT   = bool(true, "charms", "type_charms", "grant_on_advancement", "normal");
    public static final BooleanValue GRANT_POISON_CHARM_ON_ADVANCEMENT   = bool(true, "charms", "type_charms", "grant_on_advancement", "poison");
    public static final BooleanValue GRANT_PSYCHIC_CHARM_ON_ADVANCEMENT  = bool(true, "charms", "type_charms", "grant_on_advancement", "psychic");
    public static final BooleanValue GRANT_ROCK_CHARM_ON_ADVANCEMENT     = bool(true, "charms", "type_charms", "grant_on_advancement", "rock");
    public static final BooleanValue GRANT_STEEL_CHARM_ON_ADVANCEMENT    = bool(true, "charms", "type_charms", "grant_on_advancement", "steel");
    public static final BooleanValue GRANT_WATER_CHARM_ON_ADVANCEMENT    = bool(true, "charms", "type_charms", "grant_on_advancement", "water");

    // Training Items
    public static final IntValue SUPER_EV_INCREASE_AMOUNT  = intVal(100, 1, 252, "training_items", "ev_items", "ev_increase_amount");
    public static final BooleanValue ENABLE_SUPER_CARBOS   = bool(true, "training_items", "ev_items", "enable_super_carbos");
    public static final BooleanValue ENABLE_SUPER_PROTEIN  = bool(true, "training_items", "ev_items", "enable_super_protein");
    public static final BooleanValue ENABLE_SUPER_HP_UP    = bool(true, "training_items", "ev_items", "enable_super_hp_up");
    public static final BooleanValue ENABLE_SUPER_IRON     = bool(true, "training_items", "ev_items", "enable_super_iron");
    public static final BooleanValue ENABLE_SUPER_CALCIUM  = bool(true, "training_items", "ev_items", "enable_super_calcium");
    public static final BooleanValue ENABLE_SUPER_ZINC     = bool(true, "training_items", "ev_items", "enable_super_zinc");

    public static final IntValue SUPER_IV_INCREASE_AMOUNT   = intVal(10,  1, 31, "training_items", "iv_items", "iv_increase_amount");
    public static final IntValue GOLD_BOTTLE_CAP_IV_AMOUNT  = intVal(31,  1, 31, "training_items", "iv_items", "gold_bottle_cap_iv_amount");
    public static final BooleanValue ENABLE_SUPER_HEALTH_CANDY  = bool(true, "training_items", "iv_items", "enable_super_health_candy");
    public static final BooleanValue ENABLE_SUPER_MIGHTY_CANDY  = bool(true, "training_items", "iv_items", "enable_super_mighty_candy");
    public static final BooleanValue ENABLE_SUPER_TOUGH_CANDY   = bool(true, "training_items", "iv_items", "enable_super_tough_candy");
    public static final BooleanValue ENABLE_SUPER_SMART_CANDY   = bool(true, "training_items", "iv_items", "enable_super_smart_candy");
    public static final BooleanValue ENABLE_SUPER_COURAGE_CANDY = bool(true, "training_items", "iv_items", "enable_super_courage_candy");
    public static final BooleanValue ENABLE_SUPER_QUICK_CANDY   = bool(true, "training_items", "iv_items", "enable_super_quick_candy");
    public static final BooleanValue ENABLE_GOLD_BOTTLE_CAP     = bool(true, "training_items", "iv_items", "enable_gold_bottle_cap");

    // Lookup maps
    private static final Map<CharmType, BooleanValue> TYPE_CHARM_CONFIG_MAP = new EnumMap<>(CharmType.class);
    private static final Map<CharmType, BooleanValue> TYPE_CHARM_GRANT_MAP  = new EnumMap<>(CharmType.class);

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
    }

    // Public API

    public static boolean isTypeCharmEnabled(CharmType type) {
        if (!ENABLE_ALL_TYPE_CHARMS.get()) return false;
        BooleanValue value = TYPE_CHARM_CONFIG_MAP.get(type);
        return value == null || value.get();
    }

    public static boolean isTypeCharmGrantedOnAdvancement(CharmType type) {
        if (!GRANT_CHARM_ON_ADVANCEMENT.get()) return false;
        BooleanValue value = TYPE_CHARM_GRANT_MAP.get(type);
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
        SHINY_CHARM_MULTIPLIER.set(shinyCharmMultiplier);
        EXP_CHARM_MULTIPLIER.set(expCharmMultiplier);
        CATCH_CHARM_MULTIPLIER.set(catchCharmMultiplier);
        TYPE_CHARM_MATCH_MULTIPLIER.set(typeCharmMatchMultiplier);
        TYPE_CHARM_NON_MATCH_MULTIPLIER.set(typeCharmNonMatchMultiplier);
        TYPE_CHARM_RADIUS.set(typeCharmRadius);
        TYPE_CHARM_THRESHOLD_PERCENTAGE.set(typeCharmThresholdPercentage);
    }

    // Init / Load / Save

    public static void init() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("cobblemoncharms.json");
        load(configPath);
        save(configPath);
        SPEC.setLoaded();
    }

    private static void load(Path path) {
        File file = path.toFile();
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            for (BooleanValue v : ALL_BOOLS) {
                JsonElement el = getNested(root, v.getPath());
                if (el != null) v.set(el.getAsBoolean());
            }
            for (DoubleValue v : ALL_DOUBLES) {
                JsonElement el = getNested(root, v.getPath());
                if (el != null) v.set(el.getAsDouble());
            }
            for (IntValue v : ALL_INTS) {
                JsonElement el = getNested(root, v.getPath());
                if (el != null) v.set(el.getAsInt());
            }
        } catch (Exception e) {
            System.err.println("[CobblemonCharms] Failed to load config: " + e.getMessage());
        }
    }

    private static void save(Path path) {
        JsonObject root = new JsonObject();

        for (BooleanValue v : ALL_BOOLS)  setNested(root, v.getPath(), new JsonPrimitive(v.get()));
        for (DoubleValue  v : ALL_DOUBLES) setNested(root, v.getPath(), new JsonPrimitive(v.get()));
        for (IntValue     v : ALL_INTS)    setNested(root, v.getPath(), new JsonPrimitive(v.get()));

        try (Writer writer = new FileWriter(path.toFile())) {
            new GsonBuilder().setPrettyPrinting().create().toJson(root, writer);
        } catch (Exception e) {
            System.err.println("[CobblemonCharms] Failed to save config: " + e.getMessage());
        }
    }

    /**
     * Reads a nested value from a JsonObject using a path array.
     * e.g. path = ["charms", "shiny_charm", "enabled"] reads root.charms.shiny_charm.enabled
     */
    private static JsonElement getNested(JsonObject root, String[] path) {
        JsonObject current = root;
        for (int i = 0; i < path.length - 1; i++) {
            JsonElement el = current.get(path[i]);
            if (el == null || !el.isJsonObject()) return null;
            current = el.getAsJsonObject();
        }
        return current.get(path[path.length - 1]);
    }

    /**
     * Writes a value into a nested JsonObject structure, creating intermediate
     * objects as needed.
     */
    private static void setNested(JsonObject root, String[] path, JsonPrimitive value) {
        JsonObject current = root;
        for (int i = 0; i < path.length - 1; i++) {
            JsonElement el = current.get(path[i]);
            if (el == null || !el.isJsonObject()) {
                JsonObject next = new JsonObject();
                current.add(path[i], next);
                current = next;
            } else {
                current = el.getAsJsonObject();
            }
        }
        current.add(path[path.length - 1], value);
    }

    // Factory helpers

    private static BooleanValue bool(boolean def, String... path) {
        BooleanValue v = new BooleanValue(def, path);
        ALL_BOOLS.add(v);
        return v;
    }

    private static DoubleValue doubleVal(double def, double min, double max, String... path) {
        DoubleValue v = new DoubleValue(def, min, max, path);
        ALL_DOUBLES.add(v);
        return v;
    }

    private static IntValue intVal(int def, int min, int max, String... path) {
        IntValue v = new IntValue(def, min, max, path);
        ALL_INTS.add(v);
        return v;
    }
}