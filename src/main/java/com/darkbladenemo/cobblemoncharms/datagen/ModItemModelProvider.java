package com.darkbladenemo.cobblemoncharms.datagen;

import com.darkbladenemo.cobblemoncharms.CobblemonCharmsMod;
import com.darkbladenemo.cobblemoncharms.common.item.charm.CharmType;
import com.darkbladenemo.cobblemoncharms.init.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CobblemonCharmsMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.SUPER_CARBOS.get());
        basicItem(ModItems.SUPER_PROTEIN.get());
        basicItem(ModItems.SUPER_HP_UP.get());
        basicItem(ModItems.SUPER_IRON.get());
        basicItem(ModItems.SUPER_CALCIUM.get());
        basicItem(ModItems.SUPER_ZINC.get());

        basicItem(ModItems.SUPER_HEALTH_CANDY.get());
        basicItem(ModItems.SUPER_MIGHTY_CANDY.get());
        basicItem(ModItems.SUPER_TOUGH_CANDY.get());
        basicItem(ModItems.SUPER_SMART_CANDY.get());
        basicItem(ModItems.SUPER_COURAGE_CANDY.get());
        basicItem(ModItems.SUPER_QUICK_CANDY.get());
        basicItem(ModItems.GOLD_BOTTLE_CAP.get());

        basicItem(ModItems.SHINY_CHARM.get());
        basicItem(ModItems.EXP_CHARM.get());
        basicItem(ModItems.CATCH_CHARM.get());
        basicItem(ModItems.MULTI_CHARM.get());

        for (CharmType type : CharmType.getEntries()) {
            var holder = ModItems.TYPE_CHARMS.get(type);
            if (holder != null) {
                basicItem(holder.get());
            }
        }
    }
}