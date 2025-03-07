package com.xsasakihaise.hellasforms.battles.status;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.battles.BattleType;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRuleRegistry;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.AuroraVeil;
import com.pixelmonmod.pixelmon.battles.status.ElectricTerrain;
import com.pixelmonmod.pixelmon.battles.status.Screen;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.Set;

public class PlasmaVeil extends Screen {
    transient PixelmonWrapper user;
    private static final Set<String> DIRECT_DAMAGE_MOVES = Sets.newHashSet(new String[]{"Bide", "Counter", "Dragon Rage", "Endeavor", "Final Gambit", "Guardian of Alola", "Metal Burst", "Mirror Coat", "Nature's Madness", "Night Shade", "Psywave", "Seismic Toss", "Sonic Boom", "Super Fang"});

    public PlasmaVeil() {
        this((PixelmonWrapper)null, 5);
    }

    public PlasmaVeil(int turns) {
        this((PixelmonWrapper)null, turns);
    }

    public PlasmaVeil(PixelmonWrapper user, int turns) {
        super(StatusType.AuroraVeil, (BattleStatsType)null, turns, "hellasforms.effect.plasmaveil.raised", "hellasforms.effect.plasmaveil.already", "hellasforms.status.plasmaveil.woreoff");
        this.user = user;
    }

    public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
        if (user.targetIndex == 0 || user.bc.simulateMode) {
            if (!(user.bc.globalStatusController.getTerrain() instanceof ElectricTerrain)) {
                user.bc.sendToAll("pixelmon.effect.itfailed", new Object[0]);
                user.attack.moveResult.result = AttackResult.failed;
            } else if (user.hasStatus(new StatusType[]{this.type})) {
                user.bc.sendToAll("hellasforms.effect.plasmaveil.already", new Object[]{user.getNickname()});
                user.attack.moveResult.result = AttackResult.failed;
            } else if (!user.hasStatus(new StatusType[]{StatusType.Reflect})) {
                int turns = user.getUsableHeldItem().getHeldItemType() == EnumHeldItems.lightClay ? 8 : 5;
                user.addTeamStatus(((AuroraVeil)this.getNewInstance(turns)).withUser(user), user);
                user.bc.sendToAll("hellasforms.effect.plasmaveil.raised", new Object[]{user.getNickname()});
            }
        }

    }

    public PlasmaVeil withUser(PixelmonWrapper user) {
        this.user = user;
        return this;
    }

    public boolean shouldReduce(Attack a) {
        return true;
    }

    public float getDamageMultiplier(PixelmonWrapper user, PixelmonWrapper target) {
        if (target == user && user.hasStatus(new StatusType[]{StatusType.Confusion})) {
            return 1.0F;
        } else if (user.attack.didCrit) {
            return 1.0F;
        } else if (DIRECT_DAMAGE_MOVES.contains(user.attack.getMove().getAttackName())) {
            return 1.0F;
        } else {
            return target.bc.rules.getOrDefault(BattleRuleRegistry.BATTLE_TYPE) == BattleType.SINGLE ? 0.5F : 0.6666667F;
        }
    }

    protected Screen getNewInstance(int effectTurns) {
        return new AuroraVeil((PixelmonWrapper)null, effectTurns);
    }
}
