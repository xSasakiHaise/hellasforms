package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.Sharpness;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class SwordOfJustice extends Sharpness {

    @Override
    public int modifyDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack attack) {
        if (target.hasType(Element.DARK) ||
                target.hasType(Element.DRAGON) ||
                target.hasType(Element.GHOST) ||
                target.hasType(Element.POISON) ||
                target.hasType(Element.FAIRY)) {
            damage = (int) (damage * 1.2); // increase by 20%
        }
        return damage;
    }
}
