package com.xsasakihaise.hellasforms.interactions;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.pokemon.BottleCapEvent;
import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.api.pokemon.stats.IVStore;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreenPacket;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.items.BottlecapItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class InteractionBottleCap implements IInteraction {

    @Override
    public boolean processInteract(PixelmonEntity pixelmon, PlayerEntity player, Hand hand, ItemStack stack) {
        if (player.world.isRemote || hand == Hand.OFF_HAND || !(stack.getItem() instanceof BottlecapItem)) {
            return false;
        }
        final Pokemon mon = pixelmon.getPokemon();
        if (mon.getOwnerUUID() == null || !mon.getOwnerUUID().equals(player.getUniqueID())) {
            return false;
        }
        if (mon.getPokemonLevel() < 50) {
            ChatHandler.sendChat(player, "pixelmon.interaction.bottlecap.level", new Object[]{ pixelmon.getNickname() });
            return true;
        }

        final IVStore ivs = mon.getIVs();
        final BattleStatsType[] types = BattleStatsType.getEVIVStatValues();
        int sum = 0;
        boolean allHT = true;
        for (BattleStatsType t : types) {
            sum += ivs.getStat(t);
            allHT &= ivs.isHyperTrained(t);
        }
        final boolean isMax = (sum == 186);
        if (isMax || allHT) {
            ChatHandler.sendChat(player, "pixelmon.interaction.bottlecap.full", new Object[]{ pixelmon.getNickname() });
            return true;
        }

        if (Pixelmon.EVENT_BUS.post(new BottleCapEvent(pixelmon, player, null, stack))) {
            return false;
        }

        final int n = types.length;
        final int[] screenData = new int[n + 1];
        for (int i = 0; i < n; i++) {
            BattleStatsType t = types[i];
            screenData[i] = (!ivs.isHyperTrained(t) && ivs.getStat(t) != 31) ? getHTValue(t, mon) : 0;
        }
        screenData[n] = pixelmon.getEntityId();

        OpenScreenPacket.open(player, EnumGuiScreen.BottleCap, screenData);
        return true;
    }

    private static int getHTValue(BattleStatsType type, Pokemon pokemon) {
        IVStore store = pokemon.getIVs();
        boolean wasHT = store.isHyperTrained(type);
        store.setHyperTrained(type, true);
        int stat = pokemon.getStats().calculateStat(type, pokemon.getNature(), pokemon.getForm(), pokemon.getPokemonLevel());
        store.setHyperTrained(type, wasHT);
        return stat;
    }
}
