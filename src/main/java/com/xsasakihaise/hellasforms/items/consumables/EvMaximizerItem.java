package com.xsasakihaise.hellasforms.items.consumables;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.xsasakihaise.hellasforms.util.PixelmonStatTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class EvMaximizerItem extends PokemonInteractItem {
    private final Enum<?> targetStat;
    private final String successTranslation;

    public EvMaximizerItem(Enum<?> targetStat, String successTranslation) {
        this.targetStat = targetStat;
        this.successTranslation = successTranslation;
    }

    @Override
    protected boolean applyEffect(PlayerEntity player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack) {
        EVStore evStore = pokemon.getEVs();
        int current = PixelmonStatTypes.getEV(evStore, targetStat);
        if (current >= 252) {
            return false;
        }

        PixelmonStatTypes.setEV(evStore, targetStat, 252);
        pokemon.markDirty();
        return true;
    }

    @Override
    protected ITextComponent getSuccessMessage(Pokemon pokemon) {
        return new TranslationTextComponent(successTranslation, pokemon.getDisplayName());
    }

    @Override
    protected ITextComponent getFailureMessage(Pokemon pokemon) {
        return new TranslationTextComponent("item.hellasforms.generic.ev_max", pokemon.getDisplayName());
    }
}
