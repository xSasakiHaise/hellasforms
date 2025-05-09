package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import java.util.List;

public class GreekFire extends SpecialAttackBase {
    private String attackName = "GreekFire";

    public GreekFire() {
    }

    public String getAttackName() {
        return attackName;
    }

    public double modifyTypeEffectiveness(List<Element> effectiveTypes, Element moveType, double baseEffectiveness) {
        if (moveType == Element.FIRE && effectiveTypes.contains(Element.WATER)) {
            if (!effectiveTypes.contains(Element.BUG) && !effectiveTypes.contains(Element.STEEL) && !effectiveTypes.contains(Element.GRASS) && !effectiveTypes.contains(Element.ICE)) {
                return !effectiveTypes.contains(Element.FIRE) && !effectiveTypes.contains(Element.ROCK) && !effectiveTypes.contains(Element.WATER) && !effectiveTypes.contains(Element.DRAGON) ? 2.0F : 1.0F;
            } else {
                return 4.0F;
            }
        } else {
            return baseEffectiveness;
        }
    }
}