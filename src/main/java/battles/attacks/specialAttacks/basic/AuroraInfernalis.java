package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.AuroraVeil;
import com.pixelmonmod.pixelmon.battles.status.Burn;
import com.pixelmonmod.pixelmon.battles.status.Screen;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class AuroraInfernalis extends SpecialAttackBase {
    private String attackName = "AuroraInfernalis";

    public AuroraInfernalis() {
    }

    public String getAttackName() {
        return attackName;
    }

    // keep the same signature you had originally
    public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
        if (target.isFainted()) {
            if (user.hasStatus(new StatusType[]{StatusType.AuroraVeil})) {
                user.removeStatus(StatusType.AuroraVeil);
            }

            int turns = user.getUsableHeldItem().getHeldItemType() == EnumHeldItems.lightClay ? 8 : 5;
            user.addTeamStatus(((AuroraVeil) this.getNewInstance(turns)).withUser(user), user);
            user.bc.sendToAll("hellasforms.effect.aurorainfernalis.raised", new Object[]{user.getNickname()});
        }

        // Try to burn the user (self-inflicted). addStatus returns true if it was successfully applied.
        // Pass 'user' as the cause because the user is causing the burn to itself.
        if (user.addStatus(new Burn(), user)) {
            user.bc.sendToAll("hellasforms.effect.aurorainfernalis.burn.user", new Object[]{user.getNickname()});
        }

        // Try to burn the target (only if they're not fainted)
        if (!target.isFainted()) {
            if (target.addStatus(new Burn(), user)) {
                user.bc.sendToAll("hellasforms.effect.aurorainfernalis.burn.target", new Object[]{target.getNickname()});
            }
        }
    }

    protected Screen getNewInstance(int effectTurns) {
        return new AuroraVeil((PixelmonWrapper) null, effectTurns);
    }
}
