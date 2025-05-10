package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class SnowstormFury extends SpecialAttackBase {
    public void applyEffectAfterStatus(PixelmonWrapper user) {
        for(Attack attack : user.getMoveset()) {
            if (attack.getMove().hasEffect(this.getClass())) {
                attack.setDisabled(true, user);
            }
        }

    }
}
