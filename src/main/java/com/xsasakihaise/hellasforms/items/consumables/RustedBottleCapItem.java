package com.xsasakihaise.hellasforms.items.consumables;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.api.pokemon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

import java.util.Random;

public class RustedBottleCapItem extends PokemonInteractItem {
    private static final BattleStatsType[] BATTLE_STATS = BattleStatsType.getEVIVStatValues();

    private final Random random = new Random();

    @Override
    protected boolean applyEffect(Player player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack) {
        BattleStatsType chosen = BATTLE_STATS[random.nextInt(BATTLE_STATS.length)];
        IVStore store = pokemon.getIVs();
        store.setHyperTrained(chosen, false);
        store.setStat(chosen, 0);
        pokemon.markDirty();
        return true;
    }

    @Override
    protected Component getSuccessMessage(Pokemon pokemon) {
        return Component.translatable("item.pixelmon.rusted_bottle_cap.success", pokemon.getDisplayName());
    }
}
