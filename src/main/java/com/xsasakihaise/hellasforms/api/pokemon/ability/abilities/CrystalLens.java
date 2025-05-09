package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.SuperLuck;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class CrystalLens extends SuperLuck {
    public boolean ignoreEvasion(PixelmonWrapper pw, PixelmonWrapper target) {
        return true;
    }
}