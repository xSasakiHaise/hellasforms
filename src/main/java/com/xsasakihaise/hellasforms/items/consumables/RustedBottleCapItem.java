package com.xsasakihaise.hellasforms.items.consumables;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.StatType;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Random;

public class RustedBottleCapItem extends PokemonInteractItem {
    private static final StatType[] BATTLE_STATS = new StatType[]{
            StatType.HP,
            StatType.ATTACK,
            StatType.DEFENCE,
            StatType.SPECIAL_ATTACK,
            StatType.SPECIAL_DEFENCE,
            StatType.SPEED
    };

    private final Random random = new Random();

    @Override
    protected boolean applyEffect(PlayerEntity player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack) {
        StatType chosen = BATTLE_STATS[random.nextInt(BATTLE_STATS.length)];
        pokemon.getIVs().setStat(chosen, 0);
        pokemon.markDirty();
        return true;
    }

    @Override
    protected ITextComponent getSuccessMessage(Pokemon pokemon) {
        return new TranslationTextComponent("item.pixelmon.rusted_bottle_cap.success", pokemon.getDisplayName());
    }
}
