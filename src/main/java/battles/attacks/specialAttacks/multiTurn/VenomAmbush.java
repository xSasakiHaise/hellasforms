package battles.attacks.specialAttacks.multiTurn;

import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn.MultiTurnCharge;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.UnderGround;

public class VenomAmbush extends MultiTurnCharge {
    public VenomAmbush() {
        super("pixelmon.effect.dighole", UnderGround.class.getSimpleName(), StatusType.UnderGround);
    }
}
