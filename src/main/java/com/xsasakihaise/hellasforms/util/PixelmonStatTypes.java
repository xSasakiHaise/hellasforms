package com.xsasakihaise.hellasforms.util;

import com.pixelmonmod.pixelmon.api.pokemon.stats.StatType;

import java.util.Arrays;

/**
 * Utility helpers for resolving {@link StatType} constants across Pixelmon API versions.
 */
public final class PixelmonStatTypes {

    private PixelmonStatTypes() {
    }

    public static StatType resolve(String... candidates) {
        for (String candidate : candidates) {
            try {
                return StatType.valueOf(candidate);
            } catch (IllegalArgumentException ignored) {
            }
        }
        throw new IllegalArgumentException("Unable to resolve StatType for candidates " + Arrays.toString(candidates));
    }

    public static StatType defence() {
        return resolve("DEFENCE", "DEFENSE");
    }

    public static StatType specialDefence() {
        return resolve("SPECIAL_DEFENCE", "SPECIAL_DEFENSE");
    }
}
