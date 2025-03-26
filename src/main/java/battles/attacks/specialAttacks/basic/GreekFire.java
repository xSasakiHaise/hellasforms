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
            boolean isSuperEffective = true;
            for (Element type : effectiveTypes) {
                if (type != Element.WATER && type != Element.FIRE) {
                    isSuperEffective = false;
                    break;
                }
            }
            return isSuperEffective ? 2.0F : baseEffectiveness;
        } else {
            return baseEffectiveness;
        }
    }
}