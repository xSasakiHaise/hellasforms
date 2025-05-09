package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;

import java.util.List;

public class BleedingJaw extends SpecialAttackBase {
    private String attackName = "Bleeding Jaw";

    public BleedingJaw() {
    }

    public String getAttackName() {
        return attackName;
    }

    public double modifyTypeEffectiveness(List<Element> effectiveTypes, Element moveType, double baseEffectiveness) {
        if (moveType == Element.DARK && effectiveTypes.contains(Element.FAIRY)) {
            if (!effectiveTypes.contains(Element.GHOST) && !effectiveTypes.contains(Element.PSYCHIC)) {
                return !effectiveTypes.contains(Element.DARK) && !effectiveTypes.contains(Element.FIGHTING) && !effectiveTypes.contains(Element.FAIRY) ? 2.0F : 1.0F;
            } else {
                return 4.0F;
            }
        } else {
            return baseEffectiveness;
        }
    }
}