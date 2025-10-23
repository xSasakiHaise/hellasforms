package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.ElectricTerrain;
import com.pixelmonmod.pixelmon.battles.status.Hail;
import com.pixelmonmod.pixelmon.battles.status.Snow;
import com.pixelmonmod.pixelmon.battles.status.Sunny;
import com.pixelmonmod.pixelmon.battles.status.Terrain;
import com.pixelmonmod.pixelmon.battles.status.Weather;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class SuperconductiveScales extends AbstractAbility {

    // --- Switch-In Effects ---
    @Override
    public void applySwitchInEffect(PixelmonWrapper newPokemon) {
        // HadronEngine: Set Electric Terrain on switch-in
        Terrain terrain = newPokemon.bc.globalStatusController.getTerrain();
        if (!(terrain instanceof ElectricTerrain)) {
            if (terrain != null) {
                newPokemon.bc.globalStatusController.removeGlobalStatus(terrain);
            }

            ElectricTerrain et = new ElectricTerrain(newPokemon.getUsableHeldItem().getHeldItemType() == EnumHeldItems.terrainExtender);
            newPokemon.bc.sendToAll(et.langStart, new Object[0]);
            newPokemon.addGlobalStatus(et);
        }
        newPokemon.bc.sendToAll("pixelmon.abilities.hadronengine", new Object[]{newPokemon.getNickname()});

        // SnowWarning: Set Snow weather on switch-in
        if (!(newPokemon.bc.globalStatusController.getWeatherIgnoreAbility() instanceof Snow)
                && newPokemon.bc.globalStatusController.canWeatherChange(new Snow())) {
            Snow snow = new Snow();
            snow.setStartTurns(newPokemon);
            newPokemon.addGlobalStatus(snow);
            newPokemon.bc.sendToAll("pixelmon.abilities.snowwarning", new Object[]{newPokemon.getNickname()});
        }
    }

    // --- Repeated / Weather Effects ---
    @Override
    public void applyRepeatedEffect(PixelmonWrapper pokemon) {
        if (pokemon.bc == null) return;

        Weather weather = pokemon.bc.globalStatusController.getWeather();

        // DrySkin: Heal Pokémon during Hail if not at full health
        if (weather instanceof Hail && !pokemon.hasFullHealth()) {
            pokemon.bc.sendToAll("pixelmon.abilities.dryskinrain", new Object[]{pokemon.getNickname()});
            pokemon.healByPercent(12.5F);
        }

        // DrySkin: Deal damage to Pokémon during Sunny weather
        if (weather instanceof Sunny && pokemon.isAlive()) {
            pokemon.bc.sendToAll("pixelmon.abilities.dryskinsun", new Object[]{pokemon.getNickname()});
            pokemon.doBattleDamage(pokemon, (float) pokemon.getPercentMaxHealth(12.5F), DamageTypeEnum.ABILITY);
        }
    }
}
