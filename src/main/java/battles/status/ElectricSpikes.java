package battles.status;

import com.pixelmonmod.pixelmon.api.pokemon.type.Type;
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
        double effectiveness = 1.0D;
        if (pw.hasType(Type.GROUND)) {
            effectiveness = 0.0D;
        } else {
            if (pw.hasType(Type.WATER)) effectiveness *= 2.0D;
            if (pw.hasType(Type.FLYING)) effectiveness *= 2.0D;
            if (pw.hasType(Type.ELECTRIC)) effectiveness *= 0.5D;
            if (pw.hasType(Type.GRASS)) effectiveness *= 0.5D;
            if (pw.hasType(Type.DRAGON)) effectiveness *= 0.5D;
        }

        float modifier = (float) (effectiveness * 12.5F);
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
