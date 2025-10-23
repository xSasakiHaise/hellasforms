package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Sunny;
import com.pixelmonmod.pixelmon.battles.status.Weather;

public class PhotonAura extends AbstractAbility {
    public void applySwitchInEffect(PixelmonWrapper newPokemon) {
        Weather weather = newPokemon.bc.globalStatusController.getWeatherIgnoreAbility();
        if (weather instanceof Sunny) {
            newPokemon.bc.sendToAll("pixelmon.abilities.photon_aura", new Object[]{newPokemon.getNickname()});
        } else if (newPokemon.bc.globalStatusController.canWeatherChange(new Sunny())) {
            Sunny sunny = new Sunny();
            sunny.applyEffect(newPokemon, newPokemon);
            newPokemon.bc.sendToAll("pixelmon.abilities.photon_aura", new Object[]{newPokemon.getNickname()});
        }

    }

    public int[] modifyStats(PixelmonWrapper user, int[] stats) {
        if (user.bc.globalStatusController.getWeather() instanceof Sunny) {
            int var10001 = BattleStatsType.ATTACK.getStatIndex();
            stats[var10001] = (int)((double)stats[var10001] * 1.25);
            int var10002 = BattleStatsType.SPECIAL_ATTACK.getStatIndex();
            stats[var10002] = (int)((double)stats[var10002] * 1.25);
            int var10003 = BattleStatsType.EVASION.getStatIndex();
            stats[var10003] = (int)((double)stats[var10003] * 1.5);
        }

        return stats;
    }
}
