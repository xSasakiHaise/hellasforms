package com.xsasakihaise.hellasforms.items.consumables;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.items.QuestItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;

/**
 * Base class for consumables that interact directly with a targeted Pixelmon entity.
 * Subclasses only need to provide the actual effect and the localized message that
 * is sent back to the player when the interaction succeeds or fails.
 */
public abstract class PokemonInteractItem extends QuestItem {

    protected PokemonInteractItem() {
        super();
    }

    /**
     * Handles the "use item on Pixelmon" interaction and delegates the actual
     * effect to {@link #applyEffect(Player, Pokemon, PixelmonEntity, ItemStack)}.
     */
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!(target instanceof PixelmonEntity)) {
            return InteractionResult.PASS;
        }

        PixelmonEntity pixelmonEntity = (PixelmonEntity) target;
        Pokemon pokemon = pixelmonEntity.getPokemon();
        if (pokemon == null) {
            return InteractionResult.PASS;
        }

        if (!player.level().isClientSide()) {
            if (applyEffect(player, pokemon, pixelmonEntity, stack)) {
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                pixelmonEntity.setPokemon(pokemon);
                player.displayClientMessage(getSuccessMessage(pokemon), true);
            } else {
                Component failure = getFailureMessage(pokemon);
                if (failure != null) {
                    player.displayClientMessage(failure, true);
                }
            }
        }

        return InteractionResult.sidedSuccess(player.level().isClientSide());
    }

    /**
     * @return localized fallback when {@link #applyEffect} returns false.
     */
    protected Component getFailureMessage(Pokemon pokemon) {
        return Component.translatable("item.hellasforms.generic.no_effect", pokemon.getDisplayName());
    }

    /**
     * Applies the concrete effect (EV changes, experience, ability swapping, etc.).
     *
     * @return {@code true} when the stack should be consumed and the success
     * message should be sent to the player.
     */
    protected abstract boolean applyEffect(Player player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack);

    /**
     * @return localized message that explains what just happened to the Pokemon.
     */
    protected abstract Component getSuccessMessage(Pokemon pokemon);
}
