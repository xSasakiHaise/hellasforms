package com.xsasakihaise.hellasforms;

import com.pixelmonmod.pixelmon.items.QuestItem;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class BattlePassItem extends QuestItem {

    public BattlePassItem() {
        super();
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            String itemId = stack.getItem().getRegistryName().getPath(); // e.g., "battlepass_item_1"

            try {
                String numberStr = itemId.replace("battlepass_item_", "");
                int bpNumber = Integer.parseInt(numberStr);

                String command = String.format("minecraft:lp user %s parent add bp%d",
                        player.getName().getString(), bpNumber);

                CommandSource source = player.createCommandSourceStack()
                        .withPermission(4)
                        .withSuppressedOutput()
                        .withPosition(new Vector3d(player.getX(), player.getY(), player.getZ()));

                int result = world.getServer().getCommands().performCommand(source, command);

                if (result > 0) {
                    stack.shrink(1);
                    player.displayClientMessage(new StringTextComponent(
                            "§aYou claimed BattlePass Item " + bpNumber + "!"), true);
                } else {
                    player.displayClientMessage(new StringTextComponent(
                            "§cFailed to claim BattlePass reward."), true);
                }
            } catch (Exception e) {
                player.displayClientMessage(new StringTextComponent(
                        "§cInvalid BattlePass item!"), true);
                e.printStackTrace();
            }
        }
        return ActionResult.success(player.getItemInHand(hand));
    }
}
