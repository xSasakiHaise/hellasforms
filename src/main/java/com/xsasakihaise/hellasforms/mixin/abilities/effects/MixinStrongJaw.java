package com.xsasakihaise.hellasforms.mixin.abilities.effects;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.StrongJaw;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Set;

@Mixin({StrongJaw.class})
public class MixinStrongJaw extends AbstractAbility {
    @Overwrite(
            remap = false
    )
    public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
        Set<String> bitingMoves = Sets.newHashSet(new String[]{"Bite", "Bleeding Jaw", "Crunch", "Fire Fang", "Hyper Fang", "Ice Fang", "Poison Fang", "Psychic Fangs", "Thunder Fang", "Fishious Rend", "Jaw Lock"});
        if (bitingMoves.contains(a.getMove().getAttackName())) {
            power = (int)((double)power * (double)1.5F);
        }

        return new int[]{power, accuracy};
    }
}
