package com.xsasakihaise.hellasforms.mixin.abilities.effects;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.Sharpness;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Set;

/**
 * Extends the Sharpness ability whitelist with Hellas-exclusive slicing moves.
 */
@Mixin({Sharpness.class})
public class MixinSharpness extends AbstractAbility {
    @Overwrite(
            remap = false
    )
    public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
        Set<String> sharpnessMoves = Sets.newHashSet(new String[]{"Aerial Ace", "Aegis Driver", "Air Cutter", "Air Slash", "Aqua Cutter", "Behemoth Blade", "Bitter Blade", "Ceaseless Edge", "Cross Poison", "Cut", "Cutting Edge", "Fury Cutter", "Isbrand", "Kowtow Cleave", "Leaf Blade", "Night Slash", "Petal Blade", "Population Bomb", "Psyblade", "Psycho Cut", "Razor Leaf", "Razor Shell", "Sacred Sword", "Slash", "Snowstorm Fury", "Solar Blade", "Stone Axe", "X-Scissor"});
        if (sharpnessMoves.contains(a.getMove().getAttackName())) {
            power = (int)((double)power * (double)1.5F);
        }

        return new int[]{power, accuracy};
    }
}
