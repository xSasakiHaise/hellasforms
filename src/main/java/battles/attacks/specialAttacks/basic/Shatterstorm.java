package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.api.pokemon.type.Type;
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
        double effectiveness = 1.0D;
        if (pw.hasType(Type.GRASS)) effectiveness *= 2.0D;
        if (pw.hasType(Type.GROUND)) effectiveness *= 2.0D;
        if (pw.hasType(Type.FLYING)) effectiveness *= 2.0D;
        if (pw.hasType(Type.DRAGON)) effectiveness *= 2.0D;

        if (pw.hasType(Type.FIRE)) effectiveness *= 0.5D;
        if (pw.hasType(Type.WATER)) effectiveness *= 0.5D;
        if (pw.hasType(Type.ICE)) effectiveness *= 0.5D;
        if (pw.hasType(Type.STEEL)) effectiveness *= 0.5D;

        float modifier = (float) (effectiveness * 12.5F);
        return pw.getPercentMaxHealth(modifier);
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
