package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.MistyTerrain;
import com.pixelmonmod.pixelmon.battles.status.Terrain;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class NightmareRealm extends AbstractAbility {
    public void applySwitchInEffect(PixelmonWrapper newPokemon) {
        Terrain terrain = newPokemon.bc.globalStatusController.getTerrain();
        if (!(terrain instanceof MistyTerrain)) {
            if (terrain != null) {
                newPokemon.bc.globalStatusController.removeGlobalStatus(terrain);
            }

            MistyTerrain et = new MistyTerrain(newPokemon.getUsableHeldItem().getHeldItemType() == EnumHeldItems.terrainExtender);
            newPokemon.bc.sendToAll(et.langStart, new Object[0]);
            newPokemon.addGlobalStatus(et);
        }

        newPokemon.bc.sendToAll("pixelmon.abilities.nightmare_realm", new Object[]{newPokemon.getNickname()});
    }

    public int[] modifyStats(PixelmonWrapper user, int[] stats) {
        if (user.bc.globalStatusController.getTerrain() instanceof MistyTerrain) {
            int var10001 = BattleStatsType.DEFENSE.getStatIndex();
            stats[var10001] = (int)((double)stats[var10001] * (double)2.0F);
            int var10002 = BattleStatsType.SPECIAL_DEFENSE.getStatIndex();
            stats[var10002] = (int)((double)stats[var10002] * (double)2.0F);
        }

        return stats;
    }
}
