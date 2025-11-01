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
    private static final Enum<?>[] BATTLE_STATS = new Enum[]{
            PixelmonStatTypes.hp(),
            PixelmonStatTypes.attack(),
            PixelmonStatTypes.defence(),
            PixelmonStatTypes.specialAttack(),
            PixelmonStatTypes.specialDefence(),
            PixelmonStatTypes.speed()
    };

    private final Random random = new Random();

    @Override
    protected boolean applyEffect(PlayerEntity player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack) {
        Enum<?> chosen = BATTLE_STATS[random.nextInt(BATTLE_STATS.length)];
        PixelmonStatTypes.setIV(pokemon.getIVs(), chosen, 0);
        pokemon.markDirty();
        return true;
    }

    @Override
    protected ITextComponent getSuccessMessage(Pokemon pokemon) {
        return new TranslationTextComponent("item.pixelmon.rusted_bottle_cap.success", pokemon.getDisplayName());
    }
}
