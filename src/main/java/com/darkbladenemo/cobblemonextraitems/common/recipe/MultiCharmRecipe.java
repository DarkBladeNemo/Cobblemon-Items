package com.darkbladenemo.cobblemonextraitems.common.recipe;

import com.darkbladenemo.cobblemonextraitems.common.component.MultiCharmData;
import com.darkbladenemo.cobblemonextraitems.common.component.TypeCharmData;
import com.darkbladenemo.cobblemonextraitems.init.ModDataComponents;
import com.darkbladenemo.cobblemonextraitems.init.ModItems;
import com.darkbladenemo.cobblemonextraitems.init.ModRecipes;
import com.darkbladenemo.cobblemonextraitems.common.item.charm.TypeCharm;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class MultiCharmRecipe extends CustomRecipe {

    public MultiCharmRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        ItemStack multiCharm = ItemStack.EMPTY;
        ItemStack typeCharm = ItemStack.EMPTY;
        int itemCount = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                itemCount++;

                if (stack.is(ModItems.MULTI_CHARM.get())) {
                    if (!multiCharm.isEmpty()) return false;
                    multiCharm = stack;
                } else if (stack.getItem() instanceof TypeCharm) {
                    if (!typeCharm.isEmpty()) return false;
                    typeCharm = stack;
                } else {
                    return false;
                }
            }
        }

        if (itemCount != 2 || multiCharm.isEmpty() || typeCharm.isEmpty()) {
            return false;
        }

        MultiCharmData multiData = multiCharm.get(ModDataComponents.MULTI_CHARM_DATA.get());
        TypeCharmData typeData = typeCharm.get(ModDataComponents.TYPE_CHARM_DATA.get());

        if (multiData == null || typeData == null) return false;

        return !multiData.hasType(typeData.type());
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack multiCharm = ItemStack.EMPTY;
        ItemStack typeCharm = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.is(ModItems.MULTI_CHARM.get())) {
                    multiCharm = stack;
                } else if (stack.getItem() instanceof TypeCharm) {
                    typeCharm = stack;
                }
            }
        }

        if (multiCharm.isEmpty() || typeCharm.isEmpty()) {
            return ItemStack.EMPTY;
        }

        MultiCharmData multiData = multiCharm.get(ModDataComponents.MULTI_CHARM_DATA.get());
        TypeCharmData typeData = typeCharm.get(ModDataComponents.TYPE_CHARM_DATA.get());

        if (multiData == null || typeData == null) {
            return ItemStack.EMPTY;
        }

        ItemStack result = multiCharm.copy();
        // Only pass type and multiplier - radius is now global
        MultiCharmData newData = multiData.addType(
                typeData.type(),
                typeData.matchMultiplier()
        );
        result.set(ModDataComponents.MULTI_CHARM_DATA.get(), newData);

        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.MULTI_CHARM_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<MultiCharmRecipe> {
        private static final MapCodec<MultiCharmRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        CraftingBookCategory.CODEC.fieldOf("category")
                                .orElse(CraftingBookCategory.MISC)
                                .forGetter(CustomRecipe::category)
                ).apply(instance, MultiCharmRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, MultiCharmRecipe> STREAM_CODEC =
                StreamCodec.of(
                        (buf, recipe) -> buf.writeEnum(recipe.category()),
                        buf -> new MultiCharmRecipe(buf.readEnum(CraftingBookCategory.class))
                );

        @Override
        public MapCodec<MultiCharmRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MultiCharmRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}