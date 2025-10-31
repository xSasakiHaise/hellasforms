package com.xsasakihaise.hellasforms.items.consumables;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.items.QuestItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Base class for consumables that interact directly with a targeted Pixelmon entity.
 */
public abstract class PokemonInteractItem extends QuestItem {

    protected PokemonInteractItem() {
        super();
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (!(target instanceof PixelmonEntity)) {
            return ActionResultType.PASS;
        }

        PixelmonEntity pixelmonEntity = (PixelmonEntity) target;
        Pokemon pokemon = pixelmonEntity.getPokemon();
        if (pokemon == null) {
            return ActionResultType.PASS;
        }

        if (!player.level.isClientSide) {
            if (applyEffect(player, pokemon, pixelmonEntity, stack)) {
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                pixelmonEntity.setPokemon(pokemon);
                player.displayClientMessage(getSuccessMessage(pokemon), true);
            } else {
                ITextComponent failure = getFailureMessage(pokemon);
                if (failure != null) {
                    player.displayClientMessage(failure, true);
                }
            }
        }

        return ActionResultType.sidedSuccess(player.level.isClientSide);
    }

    protected ITextComponent getFailureMessage(Pokemon pokemon) {
        return new TranslationTextComponent("item.hellasforms.generic.no_effect", pokemon.getDisplayName());
    }

    protected abstract boolean applyEffect(PlayerEntity player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack);

    protected abstract ITextComponent getSuccessMessage(Pokemon pokemon);
}
