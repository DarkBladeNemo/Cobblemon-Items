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
        @JvmStatic  // Add this annotation for Java interop
        fun fromString(type: String?): CharmType? {
            if (type == null) return null
            return entries.find { it.translationKey.equals(type, ignoreCase = true) }
        }
    }
}