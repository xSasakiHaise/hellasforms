package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.ElectricTerrain;
import com.pixelmonmod.pixelmon.battles.status.Terrain;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class PlasmaAura extends AbstractAbility {
    public PlasmaAura() {
    }

    public void applySwitchInEffect(PixelmonWrapper newPokemon) {
        Terrain terrain = newPokemon.bc.globalStatusController.getTerrain();
        if (!(terrain instanceof ElectricTerrain)) {
            if (terrain != null) {
                newPokemon.bc.globalStatusController.removeGlobalStatus(terrain);
            }

            ElectricTerrain et = new ElectricTerrain(newPokemon.getUsableHeldItem().getHeldItemType() == EnumHeldItems.terrainExtender);
            newPokemon.bc.sendToAll(et.langStart, new Object[0]);
            newPokemon.addGlobalStatus(et);
        }

        newPokemon.bc.sendToAll("pixelmon.abilities.plasma_aura", new Object[]{newPokemon.getNickname()});
    }

    public int[] modifyStats(PixelmonWrapper user, int[] stats) {
        if (user.bc.globalStatusController.getTerrain() instanceof ElectricTerrain) {
            int var10001 = BattleStatsType.ATTACK.getStatIndex();
            stats[var10001] = (int)((double)stats[var10001] * 1.5);
            int var10002 = BattleStatsType.SPECIAL_ATTACK.getStatIndex();
            stats[var10002] = (int)((double)stats[var10002] * 1.5);
            int var10003 = BattleStatsType.SPEED.getStatIndex();
            stats[var10003] = (int)((double)stats[var10003] * 1.1);
        }

        return stats;
    }
}
