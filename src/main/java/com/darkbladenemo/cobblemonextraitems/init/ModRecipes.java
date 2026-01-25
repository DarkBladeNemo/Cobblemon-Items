package com.darkbladenemo.cobblemonextraitems.init;

import com.darkbladenemo.cobblemonextraitems.CobblemonExtraItemsMod;
import com.darkbladenemo.cobblemonextraitems.recipe.MultiCharmRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, CobblemonExtraItemsMod.MOD_ID);

    public static final Supplier<RecipeSerializer<MultiCharmRecipe>> MULTI_CHARM_SERIALIZER =
            RECIPE_SERIALIZERS.register("multi_charm", MultiCharmRecipe.Serializer::new);
}