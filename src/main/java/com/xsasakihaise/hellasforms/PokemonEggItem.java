package com.xsasakihaise.hellasforms;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pixelmonmod.pixelmon.items.QuestItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.vector.Vector3d;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Represents the different "mystery egg" quest rewards sold on Hellas servers.
 *
 * <p>Each egg looks up a JSON loot table under {@code assets/hellasforms/eggs}
 * and grants a random Pokemon (sometimes shiny) by dispatching the appropriate
 * Pixelmon {@code /pokegive} command.</p>
 */
public class PokemonEggItem extends QuestItem {

    public PokemonEggItem() {
        super(); // Mandatory for QuestItem
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            String itemId = stack.getItem().getRegistryName().getPath(); // e.g., "wild-egg"

            try {
                String jsonPath = "assets/hellasforms/eggs/" + itemId + "-data.json";
                InputStream stream = getClass().getClassLoader().getResourceAsStream(jsonPath);

                if (stream == null) {
                    player.displayClientMessage(new StringTextComponent(
                            "§cFailed to load egg JSON: " + jsonPath), true);
                    return ActionResult.success(stack);
                }

                // Eggs are defined as a simple map of "displayName -> pokemonSpec" pairs.
                Type type = new TypeToken<Map<String, String>>() {}.getType();
                Map<String, String> pokemonMap = new Gson().fromJson(new InputStreamReader(stream), type);

                if (!pokemonMap.isEmpty()) {
                    Object[] pokemons = pokemonMap.values().toArray();
                    String chosen = ((String) pokemons[player.getRandom().nextInt(pokemons.length)]).trim();

                    boolean isSparkly = itemId.contains("sparkly");
                    String command = isSparkly
                            ? String.format("pokegive %s %s shiny", player.getName().getString(), chosen)
                            : String.format("pokegive %s %s", player.getName().getString(), chosen);

                    CommandSource source = player.createCommandSourceStack()
                            .withPermission(4)
                            .withSuppressedOutput()
                            .withPosition(new Vector3d(player.getX(), player.getY(), player.getZ()));

                    int result = world.getServer().getCommands().performCommand(source, command);

                    if (result > 0) {
                        stack.shrink(1);
                        player.displayClientMessage(new StringTextComponent(
                                "The egg hatched into " + chosen + (isSparkly ? " (Shiny)!" : "!")), true);
                    } else {
                        player.displayClientMessage(new StringTextComponent(
                                "§cFailed to hatch the egg. Command did not succeed."), true);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                player.displayClientMessage(new StringTextComponent(
                        "§cFailed to process egg: " + itemId), true);
            }
        }

        return ActionResult.success(player.getItemInHand(hand));
    }
}
