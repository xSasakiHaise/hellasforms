package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.Sharpness;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class PlasmaBlade extends Sharpness {
    boolean usedOnce = false;

    public int modifyDamageUser(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
        if (a.getType() == Element.ELECTRIC) {
            this.usedOnce = true;
            return (int)((double)damage * (double)1.5F);
        } else {
            return damage;
        }
    }

    public void applyEndOfBattleEffect(PixelmonWrapper pokemon) {
        this.usedOnce = false;
    }

    public boolean needNewInstance() {
        return true;
    }
}
