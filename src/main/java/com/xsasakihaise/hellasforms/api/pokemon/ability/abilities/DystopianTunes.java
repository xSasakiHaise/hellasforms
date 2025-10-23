package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

import java.util.Set;

public class DystopianTunes extends AbstractAbility {

    // Buff moves that are claw-based
    private final Set<String> clawMoves = Sets.newHashSet(
            "Dragon Claw", "Shadow Claw", "Metal Claw", "Crush Claw", "Dire Claw", "Rabid Claw"
    );

    @Override
    public int modifyDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
        // Halve damage from sound-based moves
        if (a.isSoundBased()) {
            damage /= 2;
        }
        return damage;
    }

    @Override
    public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
        // Buff claw moves by 1.5x
        if (clawMoves.contains(a.getMove().getAttackName())) {
            power = (int) (power * 1.5F);
        }

        // Buff sound-based moves by 1.3x
        if (a.isSoundBased()) {
            power = (int) (power * 1.3F);
        }

        return new int[]{power, accuracy};
    }
}
