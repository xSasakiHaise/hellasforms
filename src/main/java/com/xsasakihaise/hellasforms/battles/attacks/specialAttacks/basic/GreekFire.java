package com.xsasakihaise.hellasforms.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import java.util.List;

public class GreekFire extends SpecialAttackBase {
    public GreekFire() {
    }

    public double modifyTypeEffectiveness(List<Element> effectiveTypes, Element moveType, double baseEffectiveness) {
        if (moveType == Element.FIRE && effectiveTypes.contains(Element.WATER)) {
            if (!effectiveTypes.contains(Element.GRASS) && !effectiveTypes.contains(Element.FAIRY)) {
                return !effectiveTypes.contains(Element.FIRE) && !effectiveTypes.contains(Element.GROUND) && !effectiveTypes.contains(Element.ROCK) && !effectiveTypes.contains(Element.GHOST) ? (double)2.0F : (double)1.0F;
            } else {
                return (double)4.0F;
            }
        } else {
            return baseEffectiveness;
        }
    }
}
