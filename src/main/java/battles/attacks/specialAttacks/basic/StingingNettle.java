package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.ability.Ability;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Splinters;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

import java.util.List;

public class StingingNettle extends SpecialAttackBase {

    private String attackName = "Stinging Nettle";

    public StingingNettle() {
    }

    public String getAttackName() {
        return attackName;
    }

    @Override
    public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
        if (!target.hasStatus(new StatusType[]{StatusType.Splinters})) {
            target.addStatus(new Splinters(user, 5) {

                @Override
                public void applyRepeatedEffect(PixelmonWrapper pw) {
                    Ability ability = pw.getBattleAbility();
                    if (!(ability instanceof MagicGuard)) {
                        int baseDamage = pw.getMaxHealth() / 8;

                        double multiplier = modifyTypeEffectiveness(pw.getInitialType(), Element.GRASS, 1.0);

                        int finalDamage = Math.max(1, (int) Math.round(baseDamage * multiplier));

                        pw.doBattleDamage(user, finalDamage, DamageTypeEnum.ATTACK);

                        super.applyRepeatedEffect(pw);
                    }
                }

                @Override
                public double modifyTypeEffectiveness(List<Element> effectiveTypes, Element moveType, double baseEffectiveness) {
                    if (moveType == Element.GRASS && effectiveTypes.contains(Element.WATER)) {
                        if (!effectiveTypes.contains(Element.FIRE) && !effectiveTypes.contains(Element.GRASS)) {
                            return !effectiveTypes.contains(Element.POISON) && !effectiveTypes.contains(Element.ROCK) && !effectiveTypes.contains(Element.STEEL) ? 2.0 : 1.0;
                        } else {
                            return 4.0;
                        }
                    } else {
                        return baseEffectiveness;
                    }
                }

            }, target);
        }
    }
}
