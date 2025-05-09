package battles.attacks.specialAttacks.multiTurn;

import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn.MultiTurnCharge;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Afterburner extends MultiTurnCharge {
    public Afterburner() {
        super("pixelmon.effect.afterburner");
    }

    public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
        if (this.getTurnCount(user) == 0) {
            user.getBattleStats().modifyStat(2, BattleStatsType.SPEED);
        }

        return super.applyEffectDuring(user, target);
    }
}

