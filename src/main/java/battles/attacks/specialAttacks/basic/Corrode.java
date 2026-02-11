package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.type.Type;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import net.minecraft.core.Holder;

import java.util.List;

public class Corrode extends SpecialAttackBase {
    private final String attackName = "Corrode";

    public String getAttackName() {
        return attackName;
    }

    public double modifyTypeEffectiveness(List<Holder<Type>> effectiveTypes, Holder<Type> moveType, double baseEffectiveness) {
        if (moveType.is(Type.POISON) && hasType(effectiveTypes, Type.STEEL)) {
            if (!hasType(effectiveTypes, Type.GRASS) && !hasType(effectiveTypes, Type.FAIRY)) {
                return !hasType(effectiveTypes, Type.POISON)
                        && !hasType(effectiveTypes, Type.GROUND)
                        && !hasType(effectiveTypes, Type.ROCK)
                        && !hasType(effectiveTypes, Type.GHOST) ? 2.0F : 1.0F;
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
