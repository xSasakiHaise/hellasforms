package com.xsasakihaise.hellasforms.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.AuroraVeil;
import com.pixelmonmod.pixelmon.battles.status.Screen;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class PlasmaFangs extends SpecialAttackBase {
    public PlasmaFangs() {
    }

    public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
        if (target.isFainted()) {
            if (user.hasStatus(new StatusType[]{StatusType.AuroraVeil})) {
                user.removeStatus(StatusType.AuroraVeil);
            }

            int turns = user.getUsableHeldItem().getHeldItemType() == EnumHeldItems.lightClay ? 8 : 5;
            user.addTeamStatus(((AuroraVeil)this.getNewInstance(turns)).withUser(user), user);
            user.bc.sendToAll("hellasforms.effect.plasmaveil.raised", new Object[]{user.getNickname()});
        }

    }

    protected Screen getNewInstance(int effectTurns) {
        return new AuroraVeil((PixelmonWrapper)null, effectTurns);
    }
}
