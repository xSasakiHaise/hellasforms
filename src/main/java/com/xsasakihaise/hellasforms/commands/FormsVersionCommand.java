package com.xsasakihaise.hellasforms.commands;

import com.xsasakihaise.hellasforms.HellasFormsInfoConfig;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class FormsVersionCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher, HellasFormsInfoConfig infoConfig) {
        dispatcher.register(
                Commands.literal("hellas")
                        .then(Commands.literal("forms")
                                .then(Commands.literal("version")
                                        .executes(ctx -> {
                                            if (!infoConfig.isValid()) {
                                                ctx.getSource().sendSuccess(new StringTextComponent("Fehler: HellasForms-Info nicht geladen (fehlende oder ungültige JSON)."), false);
                                                return 0;
                                            }
                                            return sendFormatted(ctx.getSource(), "Version: " + infoConfig.getVersion());
                                        })
                                )
                        )
        );
    }

    private static int sendFormatted(CommandSource source, String text) {
        source.sendSuccess(new StringTextComponent("-----------------------------------"), false);
        source.sendSuccess(new StringTextComponent(text), false);
        source.sendSuccess(new StringTextComponent("-----------------------------------"), false);
        return 1;
    }
}