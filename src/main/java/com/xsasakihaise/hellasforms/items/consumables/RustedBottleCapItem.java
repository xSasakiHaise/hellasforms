package com.xsasakihaise.hellasforms.items.consumables;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.xsasakihaise.hellasforms.util.PixelmonStatTypes;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Random;

public class RustedBottleCapItem extends PokemonInteractItem {
    private static final String[][] STAT_CANDIDATES = new String[][]{
            {"HP"},
            {"ATTACK"},
            {"DEFENCE", "DEFENSE"},
            {"SPECIAL_ATTACK", "SP_ATTACK"},
            {"SPECIAL_DEFENCE", "SPECIAL_DEFENSE", "SP_DEFENSE"},
            {"SPEED"}
    };

    private final Random random = new Random();

    @Override
    protected boolean applyEffect(PlayerEntity player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack) {
        String[] candidates = STAT_CANDIDATES[random.nextInt(STAT_CANDIDATES.length)];
        Enum<?> chosen = PixelmonStatTypes.resolve(candidates);
        PixelmonStatTypes.setIV(pokemon.getIVs(), chosen, 0);
        pokemon.markDirty();
        return true;
    }

    @Override
    protected ITextComponent getSuccessMessage(Pokemon pokemon) {
        return new TranslationTextComponent("item.pixelmon.rusted_bottle_cap.success", pokemon.getDisplayName());
    }
}
