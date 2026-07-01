package com.darkbladenemo.cobblemoncharms.advancement;

import com.darkbladenemo.cobblemoncharms.common.item.charm.CharmType;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public enum ModAdvancement {
    ROOT("root", null),
    SHINY_CHARM("dex_charm/shiny_charm", "national"),
    EXP_CHARM("utility_charm/exp_charm", null),
    EXP_CHARM_REWARDED("utility_charm/exp_charm_rewarded", null),
    CATCH_CHARM("utility_charm/catch_charm", null),
    CATCH_CHARM_REWARDED("utility_charm/catch_charm_rewarded", null),
    NORMAL_CHARM("type_charms/normal_charm", null),
    FIRE_CHARM("type_charms/fire_charm", null),
    WATER_CHARM("type_charms/water_charm", null),
    ELECTRIC_CHARM("type_charms/electric_charm", null),
    GRASS_CHARM("type_charms/grass_charm", null),
    ICE_CHARM("type_charms/ice_charm", null),
    FIGHTING_CHARM("type_charms/fighting_charm", null),
    POISON_CHARM("type_charms/poison_charm", null),
    GROUND_CHARM("type_charms/ground_charm", null),
    FLYING_CHARM("type_charms/flying_charm", null),
    PSYCHIC_CHARM("type_charms/psychic_charm", null),
    BUG_CHARM("type_charms/bug_charm", null),
    ROCK_CHARM("type_charms/rock_charm", null),
    GHOST_CHARM("type_charms/ghost_charm", null),
    DRAGON_CHARM("type_charms/dragon_charm", null),
    DARK_CHARM("type_charms/dark_charm", null),
    STEEL_CHARM("type_charms/steel_charm", null),
    FAIRY_CHARM("type_charms/fairy_charm", null),
    KANTO_DEX("dex_charm/kanto_dex", "kanto"),
    JOHTO_DEX("dex_charm/johto_dex", "johto"),
    HOENN_DEX("dex_charm/hoenn_dex", "hoenn"),
    SINNOH_DEX("dex_charm/sinnoh_dex", "sinnoh"),
    UNOVA_DEX("dex_charm/unova_dex", "unova"),
    KALOS_DEX("dex_charm/kalos_dex", "kalos"),
    ALOLA_DEX("dex_charm/alola_dex", "alola"),
    GALAR_DEX("dex_charm/galar_dex", "galar"),
    HISUI_DEX("dex_charm/hisui_dex", "hisui"),
    PALDEA_DEX("dex_charm/paldea_dex", "paldea");

    private final ResourceLocation identifier;
    private final String regionKey;

    ModAdvancement(String path, String regionKey) {
        this.identifier = ResourceLocation.fromNamespaceAndPath("cobblemoncharms", path);
        this.regionKey = regionKey;
    }

    public static AdvancementHolder getTypeCharmAdvancement(MinecraftServer server, CharmType type) {
        String fullPath = "type_charms/" + type.getTranslationKey() + "_charm";
        for (ModAdvancement adv : values()) {
            if (adv.identifier.getPath().equals(fullPath)) {
                return adv.getAdvancement(server);
            }
        }
        return null;
    }

    public AdvancementHolder getAdvancement(MinecraftServer server) {
        return server == null ? null : server.getAdvancements().get(identifier);
    }

    public AdvancementHolder getAdvancement(ServerLevel level) {
        return getAdvancement(level.getServer());
    }

    public String getRegionKey() {
        return regionKey;
    }
}