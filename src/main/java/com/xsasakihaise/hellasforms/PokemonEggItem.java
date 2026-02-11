package com.xsasakihaise.hellasforms;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pixelmonmod.pixelmon.items.QuestItem;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Represents the different "mystery egg" quest rewards sold on Hellas servers.
 *
 * Each egg loads a JSON loot table from:
 *   assets/hellasforms/eggs/<itemId>-data.json
 *
 * One Pokémon is chosen at random and granted as an EGG
 * using the Pixelmon /pokegive command.
 */
public class PokemonEggItem extends QuestItem {

    public PokemonEggItem() {
        super(); // Required by QuestItem
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide()) {
            ItemStack stack = player.getItemInHand(hand);
            String itemId = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();

            try {
                String jsonPath = "assets/hellasforms/eggs/" + itemId + "-data.json";
                InputStream stream = getClass().getClassLoader().getResourceAsStream(jsonPath);

                if (stream == null) {
                    player.displayClientMessage(
                            Component.literal("§cFailed to load egg JSON: " + jsonPath),
                            true
                    );
                    return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
                }

                // JSON format: displayName -> pokemonSpec
                Type type = new TypeToken<Map<String, String>>() {}.getType();
                Map<String, String> pokemonMap =
                        new Gson().fromJson(new InputStreamReader(stream), type);

                if (!pokemonMap.isEmpty()) {
                    Object[] pokemons = pokemonMap.values().toArray();
                    String chosen =
                            ((String) pokemons[player.getRandom().nextInt(pokemons.length)]).trim();

                    boolean isSparkly = itemId.contains("sparkly");

                    // Always append the "egg" modifier
                    String command = isSparkly
                            ? String.format(
                            "pokegive %s %s egg shiny",
                            player.getName().getString(),
                            chosen
                    )
                            : String.format(
                            "pokegive %s %s egg",
                            player.getName().getString(),
                            chosen
                    );

                    CommandSourceStack source = player.createCommandSourceStack()
                            .withPermission(4)
                            .withSuppressedOutput()
                            .withPosition(new Vec3(player.getX(), player.getY(), player.getZ()));

                    int result = world.getServer()
                            .getCommands()
                            .performPrefixedCommand(source, command);

                    if (result > 0) {
                        stack.shrink(1);
                        player.displayClientMessage(
                                Component.literal(
                                        "The egg hatched into " + chosen +
                                                (isSparkly ? " (Shiny Egg)!" : " Egg!")
                                ),
                                true
                        );
                    } else {
                        player.displayClientMessage(
                                Component.literal(
                                        "§cFailed to hatch the egg. Command did not succeed."
                                ),
                                true
                        );
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                player.displayClientMessage(
                        Component.literal("§cFailed to process egg: " + itemId),
                        true
                );
            }
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
    }
}
