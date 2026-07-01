package com.darkbladenemo.cobblemoncharms.compat.jei;

import com.darkbladenemo.cobblemoncharms.CobblemonCharmsFabric;
import com.darkbladenemo.cobblemoncharms.common.component.MultiCharmData;
import com.darkbladenemo.cobblemoncharms.common.config.Config;
import com.darkbladenemo.cobblemoncharms.common.item.charm.CharmType;
import com.darkbladenemo.cobblemoncharms.common.item.charm.TypeCharm;
import com.darkbladenemo.cobblemoncharms.init.ModDataComponents;
import com.darkbladenemo.cobblemoncharms.init.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class CobblemonCharmsJeiPlugin implements IModPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger("CobblemonCharmsJEI");
    private static final ResourceLocation PLUGIN_ID =
            ResourceLocation.fromNamespaceAndPath(CobblemonCharmsFabric.MOD_ID, "jei_plugin");

    static {
        LOGGER.info("CobblemonCharmsJeiPlugin class loaded!");
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        LOGGER.info("registerRecipes called for CobblemonCharms!");

        List<RecipeHolder<net.minecraft.world.item.crafting.CraftingRecipe>> multiCharmCombineRecipes = new ArrayList<>();

        LOGGER.info("TYPE_CHARMS size: {}", ModItems.TYPE_CHARMS.size());

        int count = 0;
        for (Map.Entry<CharmType, TypeCharm> entry : ModItems.TYPE_CHARMS.entrySet()) {
            CharmType charmType = entry.getKey();
            TypeCharm typeCharmItem = entry.getValue();

            LOGGER.info("Creating recipe for type: {}", charmType.getTranslationKey());

            ItemStack emptyMultiCharm = new ItemStack(ModItems.MULTI_CHARM);
            emptyMultiCharm.set(ModDataComponents.MULTI_CHARM_DATA, MultiCharmData.empty());

            ItemStack result = emptyMultiCharm.copy();
            float multiplier = Config.TYPE_CHARM_MATCH_MULTIPLIER.floatValue();
            MultiCharmData resultData = MultiCharmData.empty().addType(charmType, multiplier);
            result.set(ModDataComponents.MULTI_CHARM_DATA, resultData);

            NonNullList<Ingredient> ingredients = NonNullList.of(
                    Ingredient.EMPTY,
                    Ingredient.of(ModItems.MULTI_CHARM),
                    Ingredient.of(typeCharmItem)
            );

            ShapelessRecipe recipe = new ShapelessRecipe(
                    "cobblemoncharms_multi_charm_combine_" + charmType.getTranslationKey(),
                    CraftingBookCategory.MISC,
                    result,
                    ingredients
            );

            ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(
                    CobblemonCharmsFabric.MOD_ID,
                    "multi_charm_combine_" + charmType.getTranslationKey()
            );

            LOGGER.info("Adding recipe: {} with result: {}", recipeId, result.getItem().getDescriptionId());
            multiCharmCombineRecipes.add(new RecipeHolder<>(recipeId, recipe));
            count++;
        }

        LOGGER.info("Total recipes added: {}", count);

        if (!multiCharmCombineRecipes.isEmpty()) {
            LOGGER.info("Calling registration.addRecipes with {} recipes", multiCharmCombineRecipes.size());
            registration.addRecipes(RecipeTypes.CRAFTING, multiCharmCombineRecipes);
            LOGGER.info("Registration complete!");
        } else {
            LOGGER.warn("No recipes were added!");
        }
    }
}