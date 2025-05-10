package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class VampiricFangs extends AbstractAbility {
    public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
        if (a.getType() != Element.DARK) {
            return true;
        } else {
            if (a.getType() != Element.GHOST) {
                return true;
            } else {
                pokemon.healByPercent(25.0F);
                return false;
            }
        }
    }
}
