package com.darkbladenemo.cobblemoncharms.datagen;

import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.item.interactive.HyperTrainingItem;
import com.darkbladenemo.cobblemoncharms.CobblemonCharmsMod;
import com.darkbladenemo.cobblemoncharms.common.condition.ConfigCondition;
import com.darkbladenemo.cobblemoncharms.common.item.charm.CharmType;
import com.darkbladenemo.cobblemoncharms.init.ModItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.AndCondition;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    private static final Map<CharmType, net.minecraft.world.item.Item> TYPE_MATERIALS = Map.ofEntries(
            Map.entry(CharmType.NORMAL,   CobblemonItems.NORMAL_GEM),
            Map.entry(CharmType.FIRE,     CobblemonItems.FIRE_GEM),
            Map.entry(CharmType.WATER,    CobblemonItems.WATER_GEM),
            Map.entry(CharmType.ELECTRIC, CobblemonItems.ELECTRIC_GEM),
            Map.entry(CharmType.GRASS,    CobblemonItems.GRASS_GEM),
            Map.entry(CharmType.ICE,      CobblemonItems.ICE_GEM),
            Map.entry(CharmType.FIGHTING, CobblemonItems.FIGHTING_GEM),
            Map.entry(CharmType.POISON,   CobblemonItems.POISON_GEM),
            Map.entry(CharmType.GROUND,   CobblemonItems.GROUND_GEM),
            Map.entry(CharmType.FLYING,   CobblemonItems.FLYING_GEM),
            Map.entry(CharmType.PSYCHIC,  CobblemonItems.PSYCHIC_GEM),
            Map.entry(CharmType.BUG,      CobblemonItems.BUG_GEM),
            Map.entry(CharmType.ROCK,     CobblemonItems.ROCK_GEM),
            Map.entry(CharmType.GHOST,    CobblemonItems.GHOST_GEM),
            Map.entry(CharmType.DRAGON,   CobblemonItems.DRAGON_GEM),
            Map.entry(CharmType.DARK,     CobblemonItems.DARK_GEM),
            Map.entry(CharmType.STEEL,    CobblemonItems.STEEL_GEM),
            Map.entry(CharmType.FAIRY,    CobblemonItems.FAIRY_GEM)
    );

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {

        evRecipe(output, ModItems.SUPER_CARBOS.get(),  "enable_super_carbos",  Items.SUGAR,                  CobblemonItems.CARBOS);
        evRecipe(output, ModItems.SUPER_PROTEIN.get(), "enable_super_protein", Items.COOKED_BEEF,            CobblemonItems.PROTEIN);
        evRecipe(output, ModItems.SUPER_HP_UP.get(),   "enable_super_hp_up",   Items.GLISTERING_MELON_SLICE, CobblemonItems.HP_UP);
        evRecipe(output, ModItems.SUPER_IRON.get(),    "enable_super_iron",    Items.CHAIN,                  CobblemonItems.IRON);
        evRecipe(output, ModItems.SUPER_CALCIUM.get(), "enable_super_calcium", Items.BONE_MEAL,              CobblemonItems.CALCIUM);
        evRecipe(output, ModItems.SUPER_ZINC.get(),    "enable_super_zinc",    Items.FERMENTED_SPIDER_EYE,   CobblemonItems.ZINC);

        ivRecipe(output, ModItems.SUPER_HEALTH_CANDY.get(),  "enable_super_health_candy",  Items.GLISTERING_MELON_SLICE, CobblemonItems.HEALTH_CANDY);
        ivRecipe(output, ModItems.SUPER_MIGHTY_CANDY.get(),  "enable_super_mighty_candy",  Items.BLAZE_POWDER,           CobblemonItems.MIGHTY_CANDY);
        ivRecipe(output, ModItems.SUPER_TOUGH_CANDY.get(),   "enable_super_tough_candy",   Items.IRON_INGOT,             CobblemonItems.TOUGH_CANDY);
        ivRecipe(output, ModItems.SUPER_SMART_CANDY.get(),   "enable_super_smart_candy",   Items.LAPIS_LAZULI,           CobblemonItems.SMART_CANDY);
        ivRecipe(output, ModItems.SUPER_COURAGE_CANDY.get(), "enable_super_courage_candy", Items.EMERALD,                CobblemonItems.COURAGE_CANDY);
        ivRecipe(output, ModItems.SUPER_QUICK_CANDY.get(),   "enable_super_quick_candy",   Items.RABBIT_FOOT,            CobblemonItems.QUICK_CANDY);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GOLD_BOTTLE_CAP.get())
                .pattern("GGG")
                .pattern("GNG")
                .pattern("GGG")
                .define('G', Items.GOLD_INGOT)
                .define('N', Items.NETHERITE_INGOT)
                .unlockedBy("has_netherite", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_INGOT))
                .save(withCondition(output,
                        new AndCondition(List.of(
                                new ConfigCondition("enable_all_iv_items"),
                                new ConfigCondition("enable_gold_bottle_cap")
                        ))), ResourceLocation.fromNamespaceAndPath(CobblemonCharmsMod.MOD_ID, "candies/gold_bottle_cap"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SHINY_CHARM.get())
                .pattern("ACD")
                .pattern("CBC")
                .pattern("DCB")
                .define('A', Items.LEAD)
                .define('B', CobblemonItems.WATER_GEM)
                .define('C', Items.GOLD_INGOT)
                .define('D', Items.PRISMARINE_CRYSTALS)
                .unlockedBy("has_diamond", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND))
                .save(withCondition(output, new ConfigCondition("enable_shiny_charm")),
                        ResourceLocation.fromNamespaceAndPath(CobblemonCharmsMod.MOD_ID, "charms/shiny_charm"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EXP_CHARM.get())
                .pattern("ABE")
                .pattern("BCB")
                .pattern("EBD")
                .define('A', Items.CHAIN)
                .define('B', CobblemonItems.WATER_GEM)
                .define('C', Items.NETHER_STAR)
                .define('E', Items.EXPERIENCE_BOTTLE)
                .define('D', Items.BLUE_DYE)
                .unlockedBy("has_diamond", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHER_STAR))
                .save(withCondition(output, new ConfigCondition("enable_exp_charm")),
                        ResourceLocation.fromNamespaceAndPath(CobblemonCharmsMod.MOD_ID, "charms/exp_charm"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CATCH_CHARM.get())
                .pattern("ADA")
                .pattern("CBE")
                .pattern("AFA")
                .define('A', Items.GOLD_INGOT)
                .define('B', CobblemonItems.POKE_BALL)
                .define('C', CobblemonItems.WATER_GEM)
                .define('D', CobblemonItems.GROUND_GEM)
                .define('E', CobblemonItems.FLYING_GEM)
                .define('F', CobblemonItems.DRAGON_GEM)
                .unlockedBy("has_poke_ball", InventoryChangeTrigger.TriggerInstance.hasItems(CobblemonItems.POKE_BALL))
                .save(withCondition(output, new ConfigCondition("enable_catch_charm")),
                        ResourceLocation.fromNamespaceAndPath(CobblemonCharmsMod.MOD_ID, "charms/catch_charm"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MULTI_CHARM.get())
                .pattern("GDG")
                .pattern("CEA")
                .pattern("GBG")
                .define('G', Items.GOLD_INGOT)
                .define('E', Items.NETHER_STAR)
                .define('C', CobblemonItems.FIRE_GEM)
                .define('A', CobblemonItems.WATER_GEM)
                .define('B', CobblemonItems.GRASS_GEM)
                .define('D', CobblemonItems.GHOST_GEM)
                .unlockedBy("has_diamond", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHER_STAR))
                .save(withCondition(output, new ConfigCondition("enable_multi_charm")),
                        ResourceLocation.fromNamespaceAndPath(CobblemonCharmsMod.MOD_ID, "charms/multi_charm"));

        TYPE_MATERIALS.forEach((type, material) -> {
            var charmHolder = ModItems.TYPE_CHARMS.get(type);
            if (charmHolder == null) return;

            String key = type.getTranslationKey();

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, charmHolder.get())
                    .pattern(" T ")
                    .pattern("TGT")
                    .pattern("TTT")
                    .define('T', Items.GOLD_INGOT)
                    .define('G', material)
                    .unlockedBy("has_gold", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_INGOT))
                    .save(withCondition(output,
                            new AndCondition(List.of(
                                    new ConfigCondition("enable_all_type_charms"),
                                    new ConfigCondition("enable_" + key + "_charm")
                            ))), ResourceLocation.fromNamespaceAndPath(CobblemonCharmsMod.MOD_ID, "charms/" + key + "_charm"));
        });
    }

    private static void evRecipe(RecipeOutput output, Item result,
                                 String configKey, Item material,
                                 Item power) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                CobblemonCharmsMod.MOD_ID, "vitamins/" + BuiltInRegistries.ITEM.getKey(result).getPath());
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
                .pattern("MBM")
                .pattern("BPB")
                .pattern("MBM")
                .define('M', material)
                .define('P', power)
                .define('B', CobblemonItems.ENIGMA_BERRY)
                .unlockedBy("has_material", InventoryChangeTrigger.TriggerInstance.hasItems(material))
                .save(withCondition(output,
                        new AndCondition(List.of(
                                new ConfigCondition("enable_all_ev_items"),
                                new ConfigCondition(configKey)
                        ))), id);
    }

    private static void ivRecipe(RecipeOutput output, Item result,
                                 String configKey, Item material, HyperTrainingItem candyItem) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                CobblemonCharmsMod.MOD_ID, "candies/" + BuiltInRegistries.ITEM.getKey(result).getPath());
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
                .pattern("BMB")
                .pattern("MNM")
                .pattern("BMB")
                .define('M', material)
                .define('N', candyItem)
                .define('B', CobblemonItems.ENIGMA_BERRY)
                .unlockedBy("has_nether_star", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHER_STAR))
                .save(withCondition(output,
                        new AndCondition(List.of(
                                new ConfigCondition("enable_all_iv_items"),
                                new ConfigCondition(configKey)
                        ))), id);
    }

    private static RecipeOutput withCondition(RecipeOutput output, ICondition condition) {
        return output.withConditions(condition);
    }
}