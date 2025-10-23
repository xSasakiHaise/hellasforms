package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.api.registry.RegistryValue;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.EvolutionQuery;

public class PhotonConstruct extends AbstractAbility {

    @Override
    public void applyRepeatedEffectAfterStatus(PixelmonWrapper pokemon) {
        // Check if conditions for Photon Construct are met
        if (!pokemon.bc.simulateMode &&
                pokemon.getSpecies().is(new RegistryValue[]{PixelmonSpecies.ZYGARDE}) &&
                !pokemon.getForm().isForm("photon-construct") &&
                pokemon.getHealthPercent() <= 75.0F) {

            // Notify all players that the ability is activating
            pokemon.bc.sendToAll("pixelmon.abilities.photonconstruct.activate");

            // Store the original form in persistent Pixelmon data
            pokemon.pokemon.getPersistentData().putString("SrcForm", pokemon.getForm().getName());

            // Prepare evolution to 'complete' form
            pokemon.evolution = new EvolutionQuery(pokemon.pokemon, pokemon.getSpecies().getForm("photon-construct"));

            int maxHealth = pokemon.getMaxHealth();
            int deficit = pokemon.getHealthDeficit();

            // Transform into complete form
            pokemon.setForm("photon-construct");

            if (pokemon.entity != null) {
                pokemon.bc.updateFormChange(pokemon.entity);
            }

            // Recalculate HP after form change
            pokemon.recalculateMaxHP();
            pokemon.updateHPIncrease();
            pokemon.setHealth(pokemon.getMaxHealth() - deficit);
            pokemon.updateBattleDamage(maxHealth - pokemon.getMaxHealth());

            // Announce the transformation
            pokemon.bc.sendToAll("pixelmon.abilities.photonconstruct.transform", pokemon.getNickname());
        }
    }

    @Override
    public void applyEndOfBattleEffect(PixelmonWrapper pokemon) {
        // Reset to original form after battle if needed
        if (pokemon.getForm().isForm("photon-construct")) {
            int hp = pokemon.getHealth(true);
            pokemon.resetBattleEvolution();
            pokemon.setHealth(Math.min(hp, pokemon.getMaxHealth(true)));
        }
    }
}
