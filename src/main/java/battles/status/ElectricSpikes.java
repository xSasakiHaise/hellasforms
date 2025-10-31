package battles.status;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.battles.api.rules.clauses.BattleClauseRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.EntryHazard;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class ElectricSpikes extends EntryHazard {
    public ElectricSpikes() {
        super(StatusType.Steelsurge, 1);
    }

    @Override
    public boolean isTeamStatus() {
        return true;
    }

    @Override
    public int getDamage(PixelmonWrapper pw) {
        float effectiveness = Element.getTotalEffectiveness(pw.type, Element.ELECTRIC, pw.bc.rules.hasClause(BattleClauseRegistry.INVERSE_BATTLE));
        float modifier = effectiveness * 12.5F;
        return pw.getPercentMaxHealth(modifier);
    }

    @Override
    protected String getFirstLayerMessage() {
        return "hellasforms.effect.electricspikes";
    }

    @Override
    protected String getAffectedMessage() {
        return "hellasforms.status.electricspikes.affected";
    }

    @Override
    public int getAIWeight() {
        return 30;
    }

    @Override
    public EntryHazard getNewInstance() {
        return new ElectricSpikes();
    }
}
