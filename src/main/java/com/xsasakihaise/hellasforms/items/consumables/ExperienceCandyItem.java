package com.xsasakihaise.hellasforms.items.consumables;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ExperienceCandyItem extends PokemonInteractItem {
    private final int experienceAmount;
    private final String successTranslation;

    public ExperienceCandyItem(int experienceAmount, String successTranslation) {
        super();
        this.experienceAmount = experienceAmount;
        this.successTranslation = successTranslation;
    }

    @Override
    protected boolean applyEffect(PlayerEntity player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack) {
        pokemon.getExperience().addExperience(experienceAmount);
        pokemon.markDirty();
        return true;
    }

    @Override
    protected ITextComponent getSuccessMessage(Pokemon pokemon) {
        return new TranslationTextComponent(successTranslation, pokemon.getDisplayName());
    }
}
