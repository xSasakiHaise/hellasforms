// java
package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class PhalanxStance extends AbstractAbility {
    private String langKey;

    // Parameterloser Konstruktor, benötigt von Pixelmon beim Laden per Reflection
    public PhalanxStance() {
        this("ability.hellasforms.phalanx_stance");
    }

    public PhalanxStance(String langKey) {
        this.langKey = langKey;
    }

    @Override
    public boolean allowsStatChange(PixelmonWrapper pokemon, PixelmonWrapper user, StatsEffect e) {
        // Trigger nur bei Stat-Senkungen durch Gegner
        if (!e.getUser() && e.amount <= 0 && pokemon.getBattleStats().getStage(e.type) > -6) {

            // Sendet Prevent\-Message bei nicht-schadenden Attacken
            if (!Attack.dealsDamage(user.attack)) {
                user.bc.sendToAll(this.langKey, new Object[]{pokemon.getNickname()});
            }

            // Erhöhe Defense und Special Defense um 1 Stage (max +6)
            increaseStatStageSafe(pokemon, BattleStatsType.DEFENSE, 1);
            increaseStatStageSafe(pokemon, BattleStatsType.SPECIAL_DEFENSE, 1);

            return false; // Stat-Senkung verhindern
        } else {
            return true; // Andere Stat-Änderungen erlauben
        }
    }

    /**
     * Sicheres Erhöhen einer Stat-Stage ohne +6 zu überschreiten
     */
    private void increaseStatStageSafe(PixelmonWrapper pokemon, BattleStatsType stat, int amount) {
        int currentStage = pokemon.getBattleStats().getStage(stat);
        int newStage = Math.min(currentStage + amount, 6); // Max Stage +6
        pokemon.getBattleStats().setStage(stat, newStage);
    }
}