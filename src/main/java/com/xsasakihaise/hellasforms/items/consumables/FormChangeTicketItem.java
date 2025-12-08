package com.xsasakihaise.hellasforms.items.consumables;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.form.Form;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * A consumable ticket that swaps a Pokemon into a target form when available.
 * If the requested form does not exist for the species (or the Pokemon is
 * already in that form) the item is not consumed and the player receives a
 * failure message.
 */
public class FormChangeTicketItem extends PokemonInteractItem {

    private final String targetForm;
    private final String successTranslation;

    public FormChangeTicketItem(String targetForm, String successTranslation) {
        this.targetForm = targetForm;
        this.successTranslation = successTranslation;
    }

    @Override
    protected boolean applyEffect(PlayerEntity player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack) {
        Form form = pokemon.getSpecies().getForm(targetForm);
        if (form == null || pokemon.getForm().isForm(targetForm)) {
            return false;
        }

        pokemon.setForm(targetForm);
        pokemon.markDirty();
        return true;
    }

    @Override
    protected ITextComponent getSuccessMessage(Pokemon pokemon) {
        return new TranslationTextComponent(successTranslation, pokemon.getDisplayName());
    }

    @Override
    protected ITextComponent getFailureMessage(Pokemon pokemon) {
        if (pokemon.getSpecies().getForm(targetForm) == null) {
            return new TranslationTextComponent("item.hellasforms.form_change.missing", pokemon.getDisplayName(), targetForm);
        }
        if (pokemon.getForm().isForm(targetForm)) {
            return new TranslationTextComponent("item.hellasforms.form_change.already", pokemon.getDisplayName(), targetForm);
        }
        return super.getFailureMessage(pokemon);
    }
}
