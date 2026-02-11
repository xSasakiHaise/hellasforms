package com.xsasakihaise.hellasforms.items.consumables;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.api.pokemon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

/**
 * Sets one EV stat to the Pixelmon cap (252) while respecting the overall total
 * limit. Used to instantly perfect specific spreads for competitive play.
 */
public class EvMaximizerItem extends PokemonInteractItem {
    private final BattleStatsType targetStat;
    private final String successTranslation;

    public EvMaximizerItem(BattleStatsType targetStat, String successTranslation) {
        this.targetStat = targetStat;
        this.successTranslation = successTranslation;
    }

    @Override
    protected boolean applyEffect(Player player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack) {
        EVStore evStore = pokemon.getEVs();
        int current = evStore.getStat(targetStat);
        if (current >= EVStore.MAX_EVS) {
            return false;
        }

        // Compute total EVs to ensure we do not violate the 510 cap.
        int total = 0;
        for (BattleStatsType statType : BattleStatsType.getEVIVStatValues()) {
            total += evStore.getStat(statType);
        }

        int newTotal = total - current + EVStore.MAX_EVS;
        if (newTotal > EVStore.MAX_TOTAL_EVS) {
            return false;
        }

        evStore.setStat(targetStat, EVStore.MAX_EVS);
        pokemon.markDirty();
        return true;
    }

    @Override
    protected Component getSuccessMessage(Pokemon pokemon) {
        return Component.translatable(successTranslation, pokemon.getDisplayName());
    }

    @Override
    protected Component getFailureMessage(Pokemon pokemon) {
        return Component.translatable("item.hellasforms.generic.ev_max", pokemon.getDisplayName());
    }
}
