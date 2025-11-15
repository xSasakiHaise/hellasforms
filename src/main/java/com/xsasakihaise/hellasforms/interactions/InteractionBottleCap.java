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
import com.xsasakihaise.hellasforms.HellasForms;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

/**
 * Custom interaction that mirrors Pixelmon's bottle cap UI but enforces a
 * handful of Hellas-specific restrictions (owner only, level gate, IV summary).
 */
public class InteractionBottleCap implements IInteraction {

    /**
     * Opens the vanilla bottle cap GUI if the player meets all prerequisites and
     * posts a {@link BottleCapEvent} for other mods to react to.
     */
    @Override
    public boolean processInteract(PixelmonEntity pixelmon, PlayerEntity player, Hand hand, ItemStack stack) {
        if (player.level.isClientSide || hand == Hand.OFF_HAND || !(stack.getItem() instanceof BottlecapItem)) {
            return false;
        }
        final Pokemon mon = pixelmon.getPokemon();
        final UUID ownerUuid = getOwnerUUID(mon);
        if (ownerUuid == null || !ownerUuid.equals(player.getUUID())) {
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
        screenData[n] = pixelmon.getId();

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

    private static final Method OWNER_UUID_METHOD = resolveOwnerUuidAccessor();

    private static Method resolveOwnerUuidAccessor() {
        try {
            return Pokemon.class.getMethod("getOwnerUUID");
        } catch (final NoSuchMethodException primary) {
            try {
                return Pokemon.class.getMethod("getOwnerPlayerUUID");
            } catch (final NoSuchMethodException fallback) {
                HellasForms.LOGGER.error("Failed to find a compatible Pokemon owner UUID accessor", fallback);
                return null;
            }
        }
    }

    private static UUID getOwnerUUID(final Pokemon pokemon) {
        if (OWNER_UUID_METHOD == null) {
            return null;
        }
        try {
            return (UUID) OWNER_UUID_METHOD.invoke(pokemon);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            HellasForms.LOGGER.error("Failed to invoke Pokemon owner UUID accessor", e);
            return null;
        }
    }
}
