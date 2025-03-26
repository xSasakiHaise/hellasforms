package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import java.util.List;

public class HitchKick extends SpecialAttackBase {
    private String attackName = "HitchKick";

    public HitchKick() {
    }

    public String getAttackName() {
        return attackName;
    }

    public double modifyTypeEffectiveness(List<Element> effectiveTypes, Element moveType, double baseEffectiveness) {
        if (moveType == Element.FIGHTING && effectiveTypes.contains(Element.NORMAL)) {
            return 2.0F;
        } else {
            return baseEffectiveness;
        }
    }
}