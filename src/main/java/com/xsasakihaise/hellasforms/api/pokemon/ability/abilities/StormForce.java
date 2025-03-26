package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.battles.AttackCategory;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.ElectricTerrain;
import com.pixelmonmod.pixelmon.battles.status.Terrain;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.tools.MutableBoolean;

public class StormForce extends AbstractAbility {
    public StormForce() {
    }

    public void applySwitchOutEffect(PixelmonWrapper oldPokemon) {
        Terrain terrain = oldPokemon.bc.globalStatusController.getTerrain();
        if (!(terrain instanceof ElectricTerrain)) {
            if (terrain != null) {
                oldPokemon.bc.globalStatusController.removeGlobalStatus(terrain);
            }

            ElectricTerrain et = new ElectricTerrain(oldPokemon.getUsableHeldItem().getHeldItemType() == EnumHeldItems.terrainExtender);
            oldPokemon.bc.sendToAll(et.langStart, new Object[0]);
            oldPokemon.addGlobalStatus(et);
        }

    }

    public float modifyPriority(PixelmonWrapper pokemon, float priority, MutableBoolean triggered) {
        if (pokemon.attack.getAttackCategory() == AttackCategory.STATUS) {
            ++priority;
            triggered.setTrue();
        }

        return priority;
    }
}
