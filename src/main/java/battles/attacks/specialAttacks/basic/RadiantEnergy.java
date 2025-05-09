package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;

import java.util.List;

public class RadiantEnergy extends SpecialAttackBase {
    private String attackName = "RadiantEneregy";

    public RadiantEnergy() {
    }

    public String getAttackName() {
        return attackName;
    }

    public double modifyTypeEffectiveness(List<Element> effectiveTypes, Element moveType, double baseEffectiveness) {
        if (moveType == Element.ELECTRIC && effectiveTypes.contains(Element.NORMAL)) {
            return 2.0F;
        } else {
            return baseEffectiveness;
        }
    }
}