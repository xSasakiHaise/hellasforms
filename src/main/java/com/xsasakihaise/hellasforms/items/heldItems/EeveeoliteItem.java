package com.xsasakihaise.hellasforms.items.heldItems;

import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.HeldItem;
import net.minecraft.item.Item;

/**
 * Custom held item: doubles Eevee's Speed and Special Attack while held.
 */
public class EeveeoliteItem extends HeldItem {
    public EeveeoliteItem() {
        super(EnumHeldItems.other, new Item.Properties());
    }

    public int[] modifyStats(PixelmonWrapper user, int[] stats) {
        if (user.getSpecies().getName().equals("Eevee")) {
            int speed = BattleStatsType.SPEED.getStatIndex();
            stats[speed] = (int)((double)stats[speed] * (double)2.0F);
            int spatk = BattleStatsType.SPECIAL_ATTACK.getStatIndex();
            stats[spatk] = (int)((double)stats[spatk] * (double)2.0F);
            return stats;
        } else {
            return stats;
        }
    }
}
