package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.type.Type;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import net.minecraft.core.Holder;

import java.util.List;

public class HitchKick extends SpecialAttackBase {
    private final String attackName = "HitchKick";

    public String getAttackName() {
        return attackName;
    }

    public double modifyTypeEffectiveness(List<Holder<Type>> effectiveTypes, Holder<Type> moveType, double baseEffectiveness) {
        if (moveType.is(Type.FIGHTING) && effectiveTypes.stream().anyMatch(t -> t.is(Type.NORMAL))) {
            return 2.0F;
        }
        return baseEffectiveness;
    }
}
