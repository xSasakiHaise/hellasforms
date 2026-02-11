package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.type.Type;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import net.minecraft.core.Holder;

import java.util.List;

public class BleedingJaw extends SpecialAttackBase {
    private final String attackName = "Bleeding Jaw";

    public String getAttackName() {
        return attackName;
    }

    public double modifyTypeEffectiveness(List<Holder<Type>> effectiveTypes, Holder<Type> moveType, double baseEffectiveness) {
        if (moveType.is(Type.DARK) && hasType(effectiveTypes, Type.FAIRY)) {
            if (!hasType(effectiveTypes, Type.GHOST) && !hasType(effectiveTypes, Type.PSYCHIC)) {
                return !hasType(effectiveTypes, Type.DARK)
                        && !hasType(effectiveTypes, Type.FIGHTING)
                        && !hasType(effectiveTypes, Type.FAIRY) ? 2.0F : 1.0F;
            } else {
                return 4.0F;
            }
        }
        return baseEffectiveness;
    }

    private static boolean hasType(List<Holder<Type>> types, net.minecraft.resources.ResourceKey<Type> key) {
        return types.stream().anyMatch(t -> t.is(key));
    }
}
