package com.xsasakihaise.hellasforms.entities.pixelmon.interactions;

import com.xsasakihaise.hellasforms.items.HellasTMItem;
import com.pixelmonmod.pixelmon.api.battles.attack.AttackRegistry;
import com.pixelmonmod.pixelmon.api.config.PixelmonConfigProxy;
import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.api.pokemon.LearnMoveController;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.ImmutableAttack;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import java.util.Locale;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;

public class InteractionTMHellas implements IInteraction {
    public InteractionTMHellas() {
    }

    public boolean processInteract(PixelmonEntity entityPixelmon, PlayerEntity player, Hand hand, ItemStack itemstack) {
        if (player instanceof ServerPlayerEntity && itemstack.func_77973_b() instanceof HellasTMItem) {
            if (player == entityPixelmon.getOwner()) {
                HellasTMItem tm = (HellasTMItem)itemstack.func_77973_b();
                CompoundNBT nbt = itemstack.func_77978_p();
                ImmutableAttack ab = (ImmutableAttack)AttackRegistry.getAttackBase(nbt.func_74779_i("MoveName")).orElse((ImmutableAttack)null);
                if (ab == null) {
                    ChatHandler.sendChat(entityPixelmon.getOwner(), (HellasTMItem)itemstack.func_77973_b() + " is corrupted", new Object[0]);
                    return true;
                }

                if (!entityPixelmon.getForm().getMoves().getTutorMoves().contains(ab)) {
                    ChatHandler.sendChat(player, "pixelmon.interaction.tmcantlearn", new Object[]{entityPixelmon.getNickname(), ab.getTranslatedName()});
                    return true;
                }

                if (entityPixelmon.getPokemon().getMoveset().hasAttack(new ImmutableAttack[]{ab})) {
                    ChatHandler.sendChat(entityPixelmon.getOwner(), "pixelmon.interaction.tmknown", new Object[]{entityPixelmon.getNickname(), ab.getTranslatedName()});
                    return true;
                }

                if (entityPixelmon.getPokemon().getMoveset().size() >= 4) {
                    if (!player.func_184812_l_() && !PixelmonConfigProxy.getGeneral().getTMs().isAllowTMReuse()) {
                        ItemStack cost = itemstack.func_77946_l();
                        cost.func_190920_e(1);
                        LearnMoveController.sendLearnMove((ServerPlayerEntity)player, entityPixelmon.func_110124_au(), ab, LearnMoveController.itemCostCondition(cost));
                    } else {
                        LearnMoveController.sendLearnMove((ServerPlayerEntity)player, entityPixelmon.func_110124_au(), ab);
                    }
                } else {
                    entityPixelmon.getPokemon().getMoveset().add(new Attack(ab));
                    ChatHandler.sendChat(entityPixelmon.getOwner(), "pixelmon.stats.learnedmove", new Object[]{entityPixelmon.getNickname(), ab.getTranslatedName()});
                }

                entityPixelmon.update(new EnumUpdateType[]{EnumUpdateType.Moveset});
            } else {
                CompoundNBT nbt = itemstack.func_77978_p();
                ChatHandler.sendChat(entityPixelmon.getOwner(), "pixelmon.interaction.tmcantlearn", new Object[]{entityPixelmon.getNickname(), new TranslationTextComponent("attack." + nbt.func_74779_i("MoveName").replace(" ", "_").toLowerCase(Locale.ROOT) + "")});
            }

            return true;
        } else {
            return false;
        }
    }
}
