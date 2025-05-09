package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.battles.AttackCategory;
import com.pixelmonmod.pixelmon.api.battles.attack.AttackRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.Absorb;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.Optional;

public class VampiricFangs extends Absorb {
    protected VampiricFangs(Element type, String langString) {
        super(type, langString);
    }

    public void postProcessAttackOther(PixelmonWrapper pokemon, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
        if (pokemon != user && !a.fromDancer && a.isAttack(new Optional[]{AttackRegistry.AQUA_STEP, AttackRegistry.FEATHER_DANCE, AttackRegistry.FIERY_DANCE, AttackRegistry.DRAGON_DANCE, AttackRegistry.LUNAR_DANCE, AttackRegistry.PETAL_DANCE, AttackRegistry.REVELATION_DANCE, AttackRegistry.QUIVER_DANCE, AttackRegistry.SWORDS_DANCE, AttackRegistry.SWORDS_DANCE, AttackRegistry.TEETER_DANCE})) {
            a.fromDancer = true;
            this.sendActivatedMessage(pokemon);
            Attack lastUsed = pokemon.lastAttack;
            PixelmonWrapper newTarget;
            if (!a.isAttack(new Optional[]{AttackRegistry.TEETER_DANCE}) && !a.isAttack(new Optional[]{AttackRegistry.FEATHER_DANCE})) {
                if (a.getAttackCategory() == AttackCategory.STATUS) {
                    newTarget = pokemon;
                } else if (user.isSameTeam(pokemon)) {
                    newTarget = target;
                } else {
                    newTarget = user;
                }
            } else {
                newTarget = user;
            }

            pokemon.useTempAttack(a, Lists.newArrayList(new PixelmonWrapper[]{newTarget}), false);
            pokemon.lastAttack = lastUsed;
            a.fromDancer = false;
        }

    }
}