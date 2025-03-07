package com.xsasakihaise.hellasforms.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import java.util.List;

public class Corrode extends SpecialAttackBase {
    public Corrode() {
    }

    public double modifyTypeEffectiveness(List<Element> effectiveTypes, Element moveType, double baseEffectiveness) {
        if (moveType == Element.POISON && effectiveTypes.contains(Element.STEEL)) {
            if (!effectiveTypes.contains(Element.GRASS) && !effectiveTypes.contains(Element.FAIRY)) {
                return !effectiveTypes.contains(Element.POISON) && !effectiveTypes.contains(Element.GROUND) && !effectiveTypes.contains(Element.ROCK) && !effectiveTypes.contains(Element.GHOST) ? (double)2.0F : (double)1.0F;
            } else {
                return (double)4.0F;
            }
        } else {
            return baseEffectiveness;
        }
    }
}
