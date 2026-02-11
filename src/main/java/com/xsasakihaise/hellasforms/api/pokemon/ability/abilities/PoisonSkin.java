package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.ContactDamage;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Poison;

public class PoisonSkin extends ContactDamage {

    public PoisonSkin() {
        // Keep Rough Skin base behavior + messaging key
        super("pixelmon.abilities.roughskin");
    }

    @Override
    public void applyEffectOnContactUser(PixelmonWrapper user, PixelmonWrapper target) {
        // IMPORTANT: retain Rough Skin / ContactDamage retaliation
        super.applyEffectOnContactUser(user, target);

        // Additional: 30% chance to poison the attacker on contact
        if (RandomHelper.getRandomChance(0.3F) && Poison.poison(user, target, (Attack) null, false)) {
            user.bc.sendToAll("pixelmon.abilities.poisonskin", user.getNickname(), target.getNickname());
        }
    }
}
