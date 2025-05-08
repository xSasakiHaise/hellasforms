package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.battles.api.rules.clauses.BattleClauseRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.EntryHazard;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Steelsurge;

public class Shatterstorm extends EntryHazard {
    public Shatterstorm() {
        super(StatusType.Steelsurge, 1);
    }

    public boolean isTeamStatus() {
        return true;
    }

    public boolean isUnharmed(PixelmonWrapper pw) {
        return super.isUnharmed(pw) || pw.getBattleAbility() instanceof MagicGuard;
    }

    public int getDamage(PixelmonWrapper pw) {
        float effectiveness = Element.getTotalEffectiveness(pw.type, Element.ICE, pw.bc.rules.hasClause(BattleClauseRegistry.INVERSE_BATTLE));
        float modifier = effectiveness * 12.5F;
        int damage = pw.getPercentMaxHealth(modifier);
        return damage;
    }

    protected String getFirstLayerMessage() {
        return "pixelmon.effect.shatterstorm";
    }

    protected String getAffectedMessage() {
        return "pixelmon.status.shatterstorm.affected";
    }

    public int getAIWeight() {
        return 30;
    }

    public EntryHazard getNewInstance() {
        return new Steelsurge();
    }

}
