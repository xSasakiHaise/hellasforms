package battles.attacks.specialAttacks.multiTurn;

import battles.status.ElectricSpikes;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn.MultiTurnCharge;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.UnderGround;
import java.util.EnumSet;
import java.util.Set;

public class SurgeBurrow extends MultiTurnCharge {
    private static final Set<StatusType> CLEARABLE_HAZARDS = EnumSet.of(
        StatusType.StealthRock,
        StatusType.Spikes,
        StatusType.ToxicSpikes,
        StatusType.StickyWeb,
        StatusType.Steelsurge
    );

    public SurgeBurrow() {
        super("pixelmon.effect.dighole", UnderGround.class.getSimpleName(), StatusType.UnderGround);
    }

    @Override
    public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
        int turnCount = this.getTurnCount(user);
        if (turnCount == 0) {
            boolean removed = false;

            for (StatusType hazard : CLEARABLE_HAZARDS) {
                if (user.hasTeamStatus(hazard)) {
                    user.removeTeamStatus(hazard);
                    removed = true;
                }
            }

            if (removed) {
                user.bc.sendToAll("hellasforms.effect.surgeburrow.cleared", new Object[]{user.getNickname()});
            }
        }

        AttackResult result = super.applyEffectDuring(user, target);

        if (turnCount >= 1 && result == AttackResult.success && user.bc != null) {
            PixelmonWrapper opponent = target != null ? target : user.bc.getOpponent(user);

            if (opponent != null) {
                if (!opponent.hasTeamStatus(StatusType.Steelsurge)) {
                    opponent.addTeamStatus(new ElectricSpikes(), user);
                    user.bc.sendToAll("hellasforms.effect.surgeburrow.spikes", new Object[]{opponent.getNickname()});
                } else {
                    user.bc.sendToAll("hellasforms.effect.surgeburrow.already", new Object[]{opponent.getNickname()});
                }
            }
        }

        return result;
    }
}
