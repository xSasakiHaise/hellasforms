package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.type.Type;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import net.minecraft.core.Holder;

import java.util.List;

public class GreekFire extends SpecialAttackBase {
    private final String attackName = "Greek Fire";

    public String getAttackName() {
        return attackName;
    }

    @Override
    public double modifyTypeEffectiveness(List<Holder<Type>> targetTypes, Holder<Type> moveType, double baseEffectiveness) {
        if (!moveType.is(Type.FIRE)) {
            return baseEffectiveness;
        }

        double effectiveness = baseEffectiveness;

        for (Holder<Type> type : targetTypes) {
            if (type.is(Type.WATER)) {
                effectiveness *= 2.0;
            } else if (type.is(Type.FIRE) || type.is(Type.ROCK) || type.is(Type.DRAGON)) {
                effectiveness *= 0.5;
            } else if (type.is(Type.BUG) || type.is(Type.STEEL) || type.is(Type.GRASS) || type.is(Type.ICE)) {
                effectiveness *= 2.0;
            }
        }

        return effectiveness;
    }
}
