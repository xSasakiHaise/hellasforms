package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.api.registry.RegistryValue;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class CombatHedge extends AbstractAbility {
    public void applyRepeatedEffect(PixelmonWrapper pw) {
        this.tryFormChange(pw);
    }

    public void applySwitchInEffect(PixelmonWrapper newPokemon) {
        this.tryFormChange(newPokemon);
    }

    private void tryFormChange(PixelmonWrapper pw) {
        if (pw.getSpecies().is(new RegistryValue[]{PixelmonSpecies.SHAYMIN}) && pw.getPokemonLevelNum() >= 20) {
            if (pw.getForm().isForm(new String[]{"offensive"})) {
                if (pw.getHealthPercent() < 25.0F) {
                    pw.setForm("hellas");
                    pw.bc.sendToAll("pixelmon.abilities.combathedge.stop", new Object[]{pw.getNickname()});
                }
            } else if (pw.getForm().isForm(new String[]{"hellas"}) && pw.getHealthPercent() > 25.0F) {
                pw.setForm("offensive");
                pw.bc.sendToAll("pixelmon.abilities.combathedge.start", new Object[]{pw.getNickname()});
            }
        }

    }
}
