package com.xsasakihaise.hellasforms.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.SkillLink;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class HitchKick extends SpecialAttackBase {
    private transient int count = 0;

    public HitchKick() {
    }

    public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
        boolean hasSubstitute = false;
        if (user.getForm().getDimensions().getHeight() < target.getForm().getDimensions().getHeight()) {
            user.attack.setMoveAccuracy(-1);
        }

        if (this.count != 0) {
            return AttackResult.proceed;
        } else {
            boolean missed = false;
            user.inMultipleHit = true;

            while(this.count < 2 && user.isAlive() && target.isAlive() && !missed) {
                ++this.count;
                hasSubstitute = target.hasStatus(new StatusType[]{StatusType.Substitute});
                MoveResults[] results = user.useAttackOnly();

                for(MoveResults result : results) {
                    if (result.result == AttackResult.failed || result.result == AttackResult.missed) {
                        --this.count;
                        missed = true;
                    }

                    MoveResults var10000 = user.attack.moveResult;
                    var10000.damage += result.damage;
                    var10000 = user.attack.moveResult;
                    var10000.fullDamage += result.fullDamage;
                    user.attack.moveResult.accuracy = result.accuracy;
                }

                if (user.getBattleAbility() instanceof SkillLink || user.getForm().getDimensions().getHeight() < target.getForm().getDimensions().getHeight()) {
                    user.attack.setMoveAccuracy(-1);
                }
            }

            user.inMultipleHit = false;
            user.attack.playAnimation(user, target);
            if (this.count > 0) {
                user.bc.sendToAll("multiplehit.times", new Object[]{user.getNickname(), this.count});
            }

            this.count = 0;
            user.attack.setOverridePower(60);
            user.attack.savedPower = 60;
            if (user.getForm().getDimensions().getHeight() < target.getForm().getDimensions().getHeight()) {
                user.attack.setMoveAccuracy(-1);
            } else {
                user.attack.setMoveAccuracy(95);
            }

            Attack.postProcessAttackAllHits(user, target, user.attack, (float)user.attack.moveResult.damage, DamageTypeEnum.ATTACK, hasSubstitute);
            if (!hasSubstitute) {
                Attack.applyContactLate(user, target);
            }

            return AttackResult.hit;
        }
    }

    public int getCount() {
        return this.count;
    }
}
