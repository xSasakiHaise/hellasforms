package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.Sharpness;
import com.pixelmonmod.pixelmon.api.pokemon.type.Type;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class SwordOfJustice extends Sharpness {

    @Override
    public int modifyDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack attack) {
        if (target.hasType(Type.DARK) ||
                target.hasType(Type.DRAGON) ||
                target.hasType(Type.GHOST) ||
                target.hasType(Type.POISON) ||
                target.hasType(Type.FAIRY)) {
            damage = (int) (damage * 1.2); // increase by 20%
        }
        return damage;
    }
}
