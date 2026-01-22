package com.darkbladenemo.cobblemonextraitems.item.charm

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
        // Helper to get from string
        fun fromString(type: String): CharmType? {
            return values().find { it.translationKey.equals(type, ignoreCase = true) }
        }
    }
}