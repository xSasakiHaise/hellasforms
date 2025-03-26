package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import java.util.List;

public class Corrode extends SpecialAttackBase {
    private String attackName = "Corrode";

    public Corrode() {
    }

    public String getAttackName() {
        return attackName;
    }

    public double modifyTypeEffectiveness(List<Element> effectiveTypes, Element moveType, double baseEffectiveness) {
        if (moveType == Element.POISON && effectiveTypes.contains(Element.STEEL)) {
            if (!effectiveTypes.contains(Element.GRASS) && !effectiveTypes.contains(Element.FAIRY)) {
                return !effectiveTypes.contains(Element.POISON) && !effectiveTypes.contains(Element.GROUND) && !effectiveTypes.contains(Element.ROCK) && !effectiveTypes.contains(Element.GHOST) ? 2.0F : 1.0F;
            } else {
                return 4.0F;
            }
        } else {
            return baseEffectiveness;
        }
    }
}