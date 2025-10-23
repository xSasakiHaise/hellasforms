package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Rainy;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class Hydrosphere extends AbstractAbility {

    // --- Hydrosphere ---
    @Override
    public void applySwitchInEffect(PixelmonWrapper newPokemon) {
        if (!(newPokemon.bc.globalStatusController.getWeatherIgnoreAbility() instanceof Rainy)
                && newPokemon.bc.globalStatusController.canWeatherChange(new Rainy())) {
            Rainy rainy = new Rainy();
            rainy.setStartTurns(newPokemon);
            newPokemon.addGlobalStatus(rainy);
            newPokemon.bc.sendToAll("pixelmon.abilities.drizzle", new Object[]{newPokemon.getNickname()});
        }
    }

    // --- WaterBubble ---
    @Override
    public int modifyDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
        return a.getType() == Element.FIRE ? damage / 2 : damage;
    }

    @Override
    public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
        return a.getType() == Element.WATER ? new int[]{power * 2, accuracy} : new int[]{power, accuracy};
    }

    // --- WaterBubble ---
    @Override
    public boolean allowsStatus(StatusType status, PixelmonWrapper pokemon, PixelmonWrapper user) {
        if (status == StatusType.Burn) {
            pokemon.bc.sendToAll("pixelmon.abilities.waterbubble", new Object[]{pokemon.getNickname()});
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void applyRepeatedEffect(PixelmonWrapper pokemon) {
        if (pokemon.getPrimaryStatus().type == StatusType.Burn) {
            pokemon.bc.sendToAll("pixelmon.abilities.waterbubblecure", new Object[]{pokemon.getNickname()});
            pokemon.removeStatus(StatusType.Burn);
        }
    }
}
