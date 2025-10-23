package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Purification extends SpecialAttackBase {

    @Override
    public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
        // Reduce user's HP by 25%
        user.doBattleDamage(user, (float) user.getPercentMaxHealth(25.0F), DamageTypeEnum.SELF);
        user.bc.sendToAll("pixelmon.effect.purification", new Object[]{user.getNickname()});

        boolean buffedSomething = false;

        // Iterate over all allies
        for (PixelmonWrapper ally : user.getParticipant().allPokemon) {
            // Skip self
            if (ally == user) continue;

            // Cure primary status if any
            if (ally.removePrimaryStatus() != null) {
                buffedSomething = true;
                ally.bc.sendToAll("pixelmon.effect.curestatus", new Object[]{ally.getNickname()});
            }

            // Raise offensive stats (+1 ATTACK and +1 SPECIAL_ATTACK)
            ally.getBattleStats().modifyStat(1, BattleStatsType.ATTACK);
            ally.getBattleStats().modifyStat(1, BattleStatsType.SPECIAL_ATTACK);
            ally.bc.sendToAll("pixelmon.effect.statraised", new Object[]{ally.getNickname()});
            buffedSomething = true;
        }

        // If no ally was buffed or cured, the move fails
        if (!buffedSomething) {
            user.bc.sendToAll("pixelmon.effect.effectfailed", new Object[0]);
            return AttackResult.failed;
        }

        return AttackResult.succeeded;
    }
}
