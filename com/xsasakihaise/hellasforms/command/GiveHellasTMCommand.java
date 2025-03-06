package com.xsasakihaise.hellasforms.command;

import com.xsasakihaise.hellasforms.HellasForms;
import com.xsasakihaise.hellasforms.enums.HellasMoves;
import com.xsasakihaise.hellasforms.items.HellasTMItem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.text.StringTextComponent;

public class GiveHellasTMCommand {
    public GiveHellasTMCommand() {
    }

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)Commands.func_197057_a("givetmhellas").then(Commands.func_197056_a("moveName", StringArgumentType.string()).executes((context) -> execute((CommandSource)context.getSource(), StringArgumentType.getString(context, "moveName")))));
    }

    private static int execute(CommandSource source, String moveName) {
        if (source.func_197022_f() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity)source.func_197022_f();
            HellasMoves move = findMoveByName(moveName);
            if (move != null) {
                ItemStack tmItemStack = new ItemStack((IItemProvider)HellasForms.TM_HELLAS.get());
                HellasTMItem tmItem = (HellasTMItem)tmItemStack.func_77973_b();
                tmItem.setNBTData(tmItemStack, move);
                tmItem.updateCustomModelData(tmItemStack, move.getMoveType().toString());
                player.func_191521_c(tmItemStack);
                source.func_197030_a(new StringTextComponent("Given " + move.getMoveName() + " TM to " + player.func_200200_C_().getString()), true);
            } else {
                source.func_197021_a(new StringTextComponent("Unknown move name: " + moveName));
            }
        } else {
            source.func_197021_a(new StringTextComponent("This command can only be run by a player."));
        }

        return 1;
    }

    private static HellasMoves findMoveByName(String moveName) {
        for(HellasMoves move : HellasMoves.values()) {
            if (move.getMoveName().replaceAll(" ", "").equalsIgnoreCase(moveName)) {
                return move;
            }
        }

        return null;
    }
}
