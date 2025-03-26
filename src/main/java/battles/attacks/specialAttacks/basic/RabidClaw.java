package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Flinch;
import com.pixelmonmod.pixelmon.battles.status.Paralysis;

public class RabidClaw extends SpecialAttackBase {
    private String attackName = "RabidClaw";

    public RabidClaw() {
    }

    public String getAttackName() {
        return attackName;
    }

    public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
        if (this.checkChance()) {
            switch (RandomHelper.getRandomNumberBetween(0, 1)) {
                case 0:
                    Flinch.flinch(user, target);
                    break;
                case 1:
                    Paralysis.paralyze(user, target, user.attack, true);
            }
        }
    }
}