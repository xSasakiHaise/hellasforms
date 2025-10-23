package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import java.util.List;

public class GreekFire extends SpecialAttackBase {
    private String attackName = "Greek Fire";

    public GreekFire() {}

    public String getAttackName() {
        return attackName;
    }

    /**
     * Modify type effectiveness:
     * - Water types take 2× instead of being resisted.
     * - Dual types are handled correctly by multiplying each type's modifier.
     * - Other types follow standard Fire-type effectiveness.
     */
    @Override
    public double modifyTypeEffectiveness(List<Element> targetTypes, Element moveType, double baseEffectiveness) {
        // Only modify Fire-type moves
        if (moveType != Element.FIRE) {
            return baseEffectiveness;
        }

        double effectiveness = baseEffectiveness; // start from default

        for (Element type : targetTypes) {
            if (type == Element.WATER) {
                effectiveness *= 2.0; // force Water to take 2×
            } else if (type == Element.FIRE || type == Element.ROCK || type == Element.DRAGON) {
                effectiveness *= 0.5; // resisted normally
            } else if (type == Element.BUG || type == Element.STEEL || type == Element.GRASS || type == Element.ICE) {
                effectiveness *= 2.0; // weak to Fire
            } else {
                effectiveness *= 1.0; // neutral for all other types
            }
        }

        return effectiveness;
    }
}
