package com.xsasakihaise.hellasforms.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import java.util.Arrays;
import java.util.Objects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class InteractionRustedBottleCap implements IInteraction {
    public InteractionRustedBottleCap() {
    }

    public boolean processInteract(PixelmonEntity pixelmon, PlayerEntity player, Hand hand, ItemStack itemstack) {
        if (player instanceof ServerPlayerEntity && pixelmon.getOwner() == player) {
            Item item = itemstack.enchant();
            if (Objects.requireNonNull(item.getRegistryName()).toString().equals("pixelmon:rusted_bottle_cap")) {
                Pokemon pokemon = pixelmon.getPokemon();
                int[] ivs = pokemon.getIVs().getArray();
                int[] minivs = new int[]{0, 0, 0, 0, 0, 0};
                if (!Arrays.equals(ivs, minivs)) {
                    pokemon.getIVs().setHyperTrained(BattleStatsType.HP, false);
                    pokemon.getIVs().setHyperTrained(BattleStatsType.ATTACK, false);
                    pokemon.getIVs().setHyperTrained(BattleStatsType.DEFENSE, false);
                    pokemon.getIVs().setHyperTrained(BattleStatsType.SPECIAL_ATTACK, false);
                    pokemon.getIVs().setHyperTrained(BattleStatsType.SPECIAL_DEFENSE, false);
                    pokemon.getIVs().setHyperTrained(BattleStatsType.SPEED, false);
                    pokemon.getIVs().fillFromArray(minivs);
                    pokemon.setFriendship(0);
                    ChatHandler.sendChat(player, "hellasforms.interaction.rusted_bottle_cap", new Object[]{pokemon.getNickname()});
                    itemstack.enchant(1);
                } else {
                    ChatHandler.sendChat(player, "hellasforms.interaction.rusted_bottle_cap.failure", new Object[]{pokemon.getNickname()});
                }
            }

            return false;
        } else {
            return false;
        }
    }
}
