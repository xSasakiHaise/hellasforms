package com.xsasakihaise.hellasforms.items.consumables;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.ability.Ability;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbilitySlot;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AbilityPatchRemoverItem extends PokemonInteractItem {
    private final Random random = new Random();

    @Override
    protected boolean applyEffect(PlayerEntity player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack) {
        List<Ability> normalAbilities = pokemon.getBaseStats()
                .getAbilities()
                .get(AbilitySlot.NORMAL);

        if (normalAbilities == null || normalAbilities.isEmpty()) {
            return false;
        }

        List<Integer> candidateSlots = new ArrayList<>();
        for (int slot = 0; slot < normalAbilities.size(); slot++) {
            if (normalAbilities.get(slot) != null) {
                candidateSlots.add(slot);
            }
        }

        if (candidateSlots.isEmpty()) {
            return false;
        }

        int currentSlot = pokemon.getAbilitySlot();
        if (candidateSlots.size() > 1) {
            candidateSlots.removeIf(slot -> slot == currentSlot);
            if (candidateSlots.isEmpty()) {
                return false;
            }
        }

        int chosenSlot = candidateSlots.get(random.nextInt(candidateSlots.size()));
        Ability chosen = normalAbilities.get(chosenSlot);
        if (chosen == null) {
            return false;
        }

        pokemon.setAbility(chosen);
        pokemon.setAbilitySlot(chosenSlot);
        pokemon.markDirty();
        return true;
    }

    @Override
    protected ITextComponent getSuccessMessage(Pokemon pokemon) {
        return new TranslationTextComponent("item.pixelmon.ability_patch_remover.success", pokemon.getDisplayName());
    }

    @Override
    protected ITextComponent getFailureMessage(Pokemon pokemon) {
        return new TranslationTextComponent("item.hellasforms.generic.ability_fail", pokemon.getDisplayName());
    }
}
