package com.darkbladenemo.cobblemonextraitems.common.item.charm

enum class CharmType(val translationKey: String) {
    NORMAL("normal"),
    FIRE("fire"),
    WATER("water"),
    ELECTRIC("electric"),
    GRASS("grass"),
    ICE("ice"),
    FIGHTING("fighting"),
    POISON("poison"),
    GROUND("ground"),
    FLYING("flying"),
    PSYCHIC("psychic"),
    BUG("bug"),
    ROCK("rock"),
    GHOST("ghost"),
    DRAGON("dragon"),
    DARK("dark"),
    STEEL("steel"),
    FAIRY("fairy");

    companion object {
        // Build the map once at class load for O(1) lookups
        private val BY_KEY = entries.associateBy { it.translationKey.lowercase() }

        @JvmStatic
        fun fromString(type: String?): CharmType? {
            if (type == null) return null
            return BY_KEY[type.lowercase()]
        }

        /**
         * Fast conversion from Cobblemon's ElementalType to CharmType.
         * Uses the ElementalType's showdownId which is already lowercase and normalized.
         */
        @JvmStatic
        fun fromElementalType(type: com.cobblemon.mod.common.api.types.ElementalType): CharmType? {
            return BY_KEY[type.showdownId]
        }
    }
}