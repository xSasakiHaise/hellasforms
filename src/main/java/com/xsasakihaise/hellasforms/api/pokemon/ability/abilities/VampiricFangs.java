package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.battles.attack.AttackRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.Absorb;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import java.util.Optional;

public class VampiricFangs extends Absorb {
    public VampiricFangs() {
        super(Element.GHOST, "pixelmon.abilities.vampiricfangs");
    }

    public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
        return a.isAttack(new Optional[]{AttackRegistry.PHANTOM_FORCE}) && !user.hasStatus(new StatusType[]{StatusType.Vanish}) ? true : super.allowsIncomingAttack(pokemon, user, a);
    }
}
