package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.GlaiveRushLock;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class TiroFinale extends SpecialAttackBase {
    public void dealtDamage(PixelmonWrapper attacker, PixelmonWrapper defender, Attack attack, DamageTypeEnum damageType) {
        if (attack.getMove().effects.contains(this) && !attacker.hasStatus(new StatusType[]{StatusType.GlaiveRush})) {
            attacker.addStatus(new GlaiveRushLock(attacker), attacker);
        }

    }
}
