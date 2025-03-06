package com.xsasakihaise.hellasforms.items;

import com.xsasakihaise.hellasforms.enums.HellasMoves;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class HellasTMItem extends Item {
    public HellasTMItem(Item.Properties properties) {
        super(properties);
    }

    public void setNBTData(ItemStack stack, HellasMoves move) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.func_74768_a("ID", move.getId());
        nbt.func_74778_a("MoveName", move.getMoveName());
        nbt.func_74778_a("MoveType", move.getMoveType().toString());
        stack.func_77982_d(nbt);
    }

    public void updateCustomModelData(ItemStack stack, String moveType) {
        int customModelData = this.getCustomModelDataForType(moveType);
        stack.func_196082_o().func_74768_a("CustomModelData", customModelData);
    }

    public ITextComponent func_200295_i(ItemStack stack) {
        CompoundNBT nbt = stack.func_77978_p();
        if (nbt != null) {
            int id = nbt.func_74762_e("ID");
            String moveName = nbt.func_74779_i("MoveName");
            return new StringTextComponent("Hellas TM" + (id < 10 ? "0" + id : id) + ": " + moveName);
        } else {
            return super.func_200295_i(stack);
        }
    }

    private int getCustomModelDataForType(String type) {
        switch (type) {
            case "BUG":
                return 1;
            case "DARK":
                return 2;
            case "DRAGON":
                return 3;
            case "ELECTRIC":
                return 4;
            case "FAIRY":
                return 5;
            case "FIGHTING":
                return 6;
            case "FIRE":
                return 7;
            case "FLYING":
                return 8;
            case "GHOST":
                return 9;
            case "GRASS":
                return 10;
            case "GROUND":
                return 11;
            case "ICE":
                return 12;
            case "NORMAL":
                return 13;
            case "POISON":
                return 14;
            case "PSYCHIC":
                return 15;
            case "ROCK":
                return 16;
            case "STEEL":
                return 17;
            case "WATER":
                return 18;
            default:
                return 0;
        }
    }
}
