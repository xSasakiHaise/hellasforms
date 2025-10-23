package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class GraveMistake extends AbstractAbility {

    /**
     * Boosts outgoing super-effective moves by 25%
     */
    public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
        if (Element.getTotalEffectiveness(target.type, a.getType()) >= 2.0F) {
            power = (int)(power * 1.25F);
        }
        return new int[]{power, accuracy};
    }

    /**
     * Increases damage taken from super-effective moves by 25%
     */
    public float modifyDamageTaken(float damage, PixelmonWrapper defender, PixelmonWrapper attacker, Attack a) {
        if (Element.getTotalEffectiveness(defender.type, a.getType()) >= 2.0F) {
            damage *= 1.25F;
        }
        return damage;
    }
}
