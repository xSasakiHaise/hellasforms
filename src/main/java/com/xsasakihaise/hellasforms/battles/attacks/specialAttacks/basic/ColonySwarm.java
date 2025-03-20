package com.xsasakihaise.hellasforms.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Sunny;

public class ColonySwarm extends SpecialAttackBase {
    public ColonySwarm() {
    }

    public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
        int amount = user.bc.globalStatusController.getWeather() instanceof Sunny ? 2 : 1;
        user.getBattleStats().modifyStat(amount, new BattleStatsType[]{BattleStatsType.ATTACK, BattleStatsType.SPEED});
        return AttackResult.succeeded;
    }
}