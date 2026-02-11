package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class IcyImmolation extends SpecialAttackBase {

    @Override
    public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
        float hpToReduce = (float) user.getPercentMaxHealth(50.0F);
        user.doBattleDamage(user, hpToReduce, DamageTypeEnum.SELF);

        user.bc.sendToAll("pixelmon.effect.icyimmolation", new Object[]{user.getNickname()});

        return AttackResult.succeeded;
    }
}