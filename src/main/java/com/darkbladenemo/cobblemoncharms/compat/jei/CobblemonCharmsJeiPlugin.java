package com.darkbladenemo.cobblemoncharms.compat.jei;

import com.darkbladenemo.cobblemoncharms.CobblemonCharmsMod;
import com.darkbladenemo.cobblemoncharms.common.component.MultiCharmData;
import com.darkbladenemo.cobblemoncharms.common.config.Config;
import com.darkbladenemo.cobblemoncharms.init.ModDataComponents;
import com.darkbladenemo.cobblemoncharms.init.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.core.NonNullList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Manually registers the MultiCharm combination recipes with JEI, since the custom
 * recipe type has no fixed pattern and can't be auto-discovered.
 * <p>
 * One entry is registered per enabled type charm: Empty MultiCharm + TypeCharm → MultiCharm
 * with that type. Skips the whole thing if Multi-Charm is disabled, and skips individual
 * types that are disabled in config, so JEI doesn't advertise recipes players can't use.
 */
@JeiPlugin
public class CobblemonCharmsJeiPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID =
            ResourceLocation.fromNamespaceAndPath(CobblemonCharmsMod.MOD_ID, "jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (!Config.ENABLE_MULTI_CHARM.get()) return;

        List<RecipeHolder<net.minecraft.world.item.crafting.CraftingRecipe>> multiCharmCombineRecipes = new ArrayList<>();

        ModItems.TYPE_CHARMS.forEach((type, deferredCharm) -> {
            if (!Config.isTypeCharmEnabled(type)) return;

            ItemStack emptyMultiCharm = new ItemStack(ModItems.MULTI_CHARM.get());
            emptyMultiCharm.set(ModDataComponents.MULTI_CHARM_DATA.get(), MultiCharmData.empty());

            ItemStack result = emptyMultiCharm.copy();
            float multiplier = Config.TYPE_CHARM_MATCH_MULTIPLIER.get().floatValue();
            MultiCharmData resultData = MultiCharmData.empty().addType(type, multiplier);
            result.set(ModDataComponents.MULTI_CHARM_DATA.get(), resultData);

            NonNullList<Ingredient> ingredients = NonNullList.of(
                    Ingredient.EMPTY,
                    Ingredient.of(ModItems.MULTI_CHARM.get()),
                    Ingredient.of(deferredCharm.get())
            );

            ShapelessRecipe recipe = new ShapelessRecipe(
                    "cobblemoncharms_multi_charm_combine",
                    CraftingBookCategory.MISC,
                    result,
                    ingredients
            );

            ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(
                    CobblemonCharmsMod.MOD_ID,
                    "multi_charm_combine_" + type.getTranslationKey()
            );
            multiCharmCombineRecipes.add(new RecipeHolder<>(recipeId, recipe));
        });

        registration.addRecipes(RecipeTypes.CRAFTING, multiCharmCombineRecipes);
    }
}