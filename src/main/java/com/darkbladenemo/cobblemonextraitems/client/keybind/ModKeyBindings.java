package com.darkbladenemo.cobblemonextraitems.client.keybind;

import com.darkbladenemo.cobblemonextraitems.CobblemonExtraItemsMod;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static final String CATEGORY = "key.categories." + CobblemonExtraItemsMod.MOD_ID;

    public static final KeyMapping OPEN_MULTI_CHARM_GUI = new KeyMapping(
            "key." + CobblemonExtraItemsMod.MOD_ID + ".open_multi_charm",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V, // Default key is V
            CATEGORY
    );
}