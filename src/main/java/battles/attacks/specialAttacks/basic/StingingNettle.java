package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.ability.Ability;
import com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.api.pokemon.type.Type;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Splinters;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import net.minecraft.core.Holder;

import java.util.List;

public class StingingNettle extends SpecialAttackBase {

    private final String attackName = "Stinging Nettle";

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

                        double multiplier = modifyTypeEffectiveness(pw.getInitialType(), resolveGrassType(pw), 1.0);

                        int finalDamage = Math.max(1, (int) Math.round(baseDamage * multiplier));

                        pw.doBattleDamage(user, finalDamage, DamageTypeEnum.ATTACK);

                        super.applyRepeatedEffect(pw);
                    }
                }

                @Override
                public double modifyTypeEffectiveness(List<Holder<Type>> effectiveTypes, Holder<Type> moveType, double baseEffectiveness) {
                    if (moveType.is(Type.GRASS) && hasType(effectiveTypes, Type.WATER)) {
                        if (!hasType(effectiveTypes, Type.FIRE) && !hasType(effectiveTypes, Type.GRASS)) {
                            return !hasType(effectiveTypes, Type.POISON)
                                    && !hasType(effectiveTypes, Type.ROCK)
                                    && !hasType(effectiveTypes, Type.STEEL) ? 2.0 : 1.0;
                        } else {
                            return 4.0;
                        }
                    }
                    return baseEffectiveness;
                }
            }, target);
        }
    }

    private static boolean hasType(List<Holder<Type>> types, net.minecraft.resources.ResourceKey<Type> key) {
        return types.stream().anyMatch(t -> t.is(key));
    }

    private Holder<Type> resolveGrassType(PixelmonWrapper pw) {
        return pw.getTypes().stream().filter(t -> t.is(Type.GRASS)).findFirst().orElse(pw.getTypes().get(0));
    }
}
