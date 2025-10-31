package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Sandstorm;

public class SandInvasion extends AbstractAbility {
    @Override
    public void applySwitchInEffect(PixelmonWrapper newPokemon) {
        if (newPokemon.bc == null) {
            return;
        }

        Sandstorm sandstorm = new Sandstorm();
        if (!(newPokemon.bc.globalStatusController.getWeatherIgnoreAbility() instanceof Sandstorm)
                && newPokemon.bc.globalStatusController.canWeatherChange(sandstorm)) {
            sandstorm.setStartTurns(newPokemon);
            newPokemon.addGlobalStatus(sandstorm);
            newPokemon.bc.sendToAll("pixelmon.abilities.sandstream", new Object[]{newPokemon.getNickname()});
        }
    }

    @Override
    public int[] modifyStats(PixelmonWrapper user, int[] stats) {
        if (user.bc != null && user.bc.globalStatusController.getWeatherIgnoreAbility() instanceof Sandstorm) {
            int speedIndex = BattleStatsType.SPEED.getStatIndex();
            stats[speedIndex] = (int) ((double) stats[speedIndex] * 2.0);
        }

        return stats;
    }
}
