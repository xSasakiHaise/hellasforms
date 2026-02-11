package com.xsasakihaise.hellasforms;

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

/**
 * Lightweight quest token that increments a player's LuckPerms track when used.
 *
 * <p>The integer suffix of the registry name (battlepass_item_1..20) determines
 * which LP parent is added.</p>
 */
public class BattlePassItem extends QuestItem {

    public BattlePassItem() {
        super();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide()) {
            ItemStack stack = player.getItemInHand(hand);
            String itemId = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();

            try {
                String numberStr = itemId.replace("battlepass_item_", "");
                int bpNumber = Integer.parseInt(numberStr);

                // Execute against LuckPerms so staff do not need to manually grant rewards.
                String command = String.format("minecraft:lp user %s parent add bp%d",
                        player.getName().getString(), bpNumber);

                CommandSourceStack source = player.createCommandSourceStack()
                        .withPermission(4)
                        .withSuppressedOutput()
                        .withPosition(new Vec3(player.getX(), player.getY(), player.getZ()));

                int result = world.getServer().getCommands().performPrefixedCommand(source, command);

                if (result > 0) {
                    stack.shrink(1);
                    player.displayClientMessage(Component.literal(
                            "§aYou claimed BattlePass Item " + bpNumber + "!"), true);
                } else {
                    player.displayClientMessage(Component.literal(
                            "§cFailed to claim BattlePass reward."), true);
                }
            } catch (Exception e) {
                player.displayClientMessage(Component.literal(
                        "§cInvalid BattlePass item!"), true);
                e.printStackTrace();
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
    }
}
