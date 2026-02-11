package com.xsasakihaise.hellasforms.items.consumables;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

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
    protected boolean applyEffect(Player player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack) {
        boolean hasTargetForm = pokemon.getSpecies().getForm(targetForm) != null;
        if (!hasTargetForm || pokemon.getForm().isForm(targetForm)) {
            return false;
        }

        pokemon.setForm(targetForm);
        pokemon.markDirty();
        return true;
    }

    @Override
    protected Component getSuccessMessage(Pokemon pokemon) {
        return Component.translatable(successTranslation, pokemon.getDisplayName());
    }

    @Override
    protected Component getFailureMessage(Pokemon pokemon) {
        if (pokemon.getSpecies().getForm(targetForm) == null) {
            return Component.translatable("item.hellasforms.form_change.missing", pokemon.getDisplayName(), targetForm);
        }
        if (pokemon.getForm().isForm(targetForm)) {
            return Component.translatable("item.hellasforms.form_change.already", pokemon.getDisplayName(), targetForm);
        }
        return super.getFailureMessage(pokemon);
    }
}
