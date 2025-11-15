package com.xsasakihaise.hellasforms.listener;

import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Ensures battle participants keep their held items after staff-organised duels.
 * Wild encounters are ignored so the vanilla "item steal" behaviour still applies.
 */
public class ReturnItemsListener {
    public ReturnItemsListener() {
    }

    @SubscribeEvent
    public void onBattleStarted(BattleStartedEvent event) {
        for(BattleParticipant p : event.getTeamOne()) {
            if (p instanceof WildPixelmonParticipant) {
                return;
            }
        }

        for(BattleParticipant p : event.getTeamTwo()) {
            if (p instanceof WildPixelmonParticipant) {
                return;
            }
        }

        for(BattleParticipant participant : event.getTeamOne()) {
            for(PixelmonWrapper pixelmonWrapper : participant.allPokemon) {
                pixelmonWrapper.enableReturnHeldItem();
            }
        }

        for(BattleParticipant participant : event.getTeamTwo()) {
            for(PixelmonWrapper pixelmonWrapper : participant.allPokemon) {
                pixelmonWrapper.enableReturnHeldItem();
            }
        }

    }
}
