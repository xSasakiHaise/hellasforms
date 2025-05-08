package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.ContactDamage;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Poison;

public class PoisonSkin extends ContactDamage {
    private final AbstractAbility abstractAbility = new AbstractAbility() {
        @Override
        public void applyEffectOnContactUser(PixelmonWrapper user, PixelmonWrapper target) {
            if (RandomHelper.getRandomChance(0.3F) && Poison.poison(user, target, (Attack) null, false)) {
                user.bc.sendToAll("pixelmon.abilities.poisonskin", new Object[]{user.getNickname(), target.getNickname()});
            }
        }
    };

    public PoisonSkin() {
        super("pixelmon.abilities.roughskin");
    }

    public void applyEffectOnContactUser(PixelmonWrapper user, PixelmonWrapper target) {
        abstractAbility.applyEffectOnContactUser(user, target);
    }
}