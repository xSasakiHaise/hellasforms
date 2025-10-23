package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class VampiricFangs extends AbstractAbility {

    @Override
    public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
        // Check if the attack is Dark or Ghost type
        if (a.getType() == Element.DARK || a.getType() == Element.GHOST) {
            // Heal 25% of max health instead of taking damage
            pokemon.healByPercent(25.0F);
            return false; // prevent damage
        }
        return true; // allow all other attacks
    }
}
