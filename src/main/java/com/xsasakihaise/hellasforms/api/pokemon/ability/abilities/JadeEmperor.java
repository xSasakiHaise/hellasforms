package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.MistyTerrain;
import com.pixelmonmod.pixelmon.battles.status.PsychicTerrain;
import com.pixelmonmod.pixelmon.battles.status.Terrain;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

/**
 * Ability used by custom forms that summons Psychic Terrain on switch-in and
 * gains an additional stat boost whenever Misty Terrain is active.
 */
public class JadeEmperor extends AbstractAbility {
    public JadeEmperor() {
    }

    public void applySwitchInEffect(PixelmonWrapper newPokemon) {
        Terrain terrain = newPokemon.bc.globalStatusController.getTerrain();
        if (!(terrain instanceof PsychicTerrain)) {
            if (terrain != null) {
                newPokemon.bc.globalStatusController.removeGlobalStatus(terrain);
            }

            PsychicTerrain et = new PsychicTerrain(newPokemon.getUsableHeldItem().getHeldItemType() == EnumHeldItems.terrainExtender);
            newPokemon.bc.sendToAll(et.langStart, new Object[0]);
            newPokemon.addGlobalStatus(et);
        }

        newPokemon.bc.sendToAll("pixelmon.abilities.jade_emperor", new Object[]{newPokemon.getNickname()});
    }

    public int[] modifyStats(PixelmonWrapper user, int[] stats) {
        if (user.bc.globalStatusController.getTerrain() instanceof MistyTerrain) {
            int var10001 = BattleStatsType.ATTACK.getStatIndex();
            stats[var10001] = (int)((double)stats[var10001] * 1.5);
            int var10002 = BattleStatsType.SPECIAL_ATTACK.getStatIndex();
            stats[var10002] = (int)((double)stats[var10002] * 1.5);
        }

        return stats;
    }
}
