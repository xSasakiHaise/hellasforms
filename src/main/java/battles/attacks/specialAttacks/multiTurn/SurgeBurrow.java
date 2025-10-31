package battles.attacks.specialAttacks.multiTurn;

import battles.status.ElectricSpikes;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn.MultiTurnCharge;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.EntryHazard;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.UnderGround;

import java.util.EnumSet;
import java.util.List;
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
                if (hasTeamStatus(user, hazard)) {
                    removed |= user.removeTeamStatus(hazard);
                }
            }

            if (removed) {
                user.bc.sendToAll("hellasforms.effect.surgeburrow.cleared", new Object[]{user.getNickname()});
            }
        }

        AttackResult result = super.applyEffectDuring(user, target);

        if (turnCount >= 1 && result != null && result.isSuccess() && user.bc != null) {
            PixelmonWrapper opponent = target != null ? target : findFirstOpponent(user);

            if (opponent != null) {
                if (!hasTeamStatus(opponent, StatusType.Steelsurge)) {
                    opponent.addTeamStatus(new ElectricSpikes(), user);
                    user.bc.sendToAll("hellasforms.effect.surgeburrow.spikes", new Object[]{opponent.getNickname()});
                } else {
                    user.bc.sendToAll("hellasforms.effect.surgeburrow.already", new Object[]{opponent.getNickname()});
                }
            }
        }

        return result;
    }

    private boolean hasTeamStatus(PixelmonWrapper wrapper, StatusType type) {
        if (wrapper == null || type == null) {
            return false;
        }
        List<EntryHazard> hazards = wrapper.getEntryHazards();
        if (hazards != null) {
            for (EntryHazard hazard : hazards) {
                if (matchesType(hazard, type)) {
                    return true;
                }
            }
        }
        List<StatusBase> statuses = wrapper.getStatuses();
        if (statuses != null) {
            for (StatusBase status : statuses) {
                if (matchesType(status, type) && status.isTeamStatus()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchesType(StatusBase status, StatusType type) {
        return status != null && status.type == type;
    }

    private PixelmonWrapper findFirstOpponent(PixelmonWrapper user) {
        if (user == null) {
            return null;
        }
        List<PixelmonWrapper> opponents = user.getOpponentPokemon();
        if (opponents != null && !opponents.isEmpty()) {
            return opponents.get(0);
        }
        return null;
    }
}
