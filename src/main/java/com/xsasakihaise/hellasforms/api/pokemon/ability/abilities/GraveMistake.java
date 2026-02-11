package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.type.Type;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class GraveMistake extends AbstractAbility {

    /**
     * Boosts outgoing super-effective moves by 25%
     */
    public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
        if (Type.getTotalEffectiveness(target.getTypes(), a.getType(), false) >= 2.0D) {
            power = (int) (power * 1.25F);
        }
        return new int[]{power, accuracy};
    }

    /**
     * Increases damage taken from super-effective moves by 25%
     */
    public float modifyDamageTaken(float damage, PixelmonWrapper defender, PixelmonWrapper attacker, Attack a) {
        if (Type.getTotalEffectiveness(defender.getTypes(), a.getType(), false) >= 2.0D) {
            damage *= 1.25F;
        }
        return damage;
    }
}
