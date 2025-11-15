package com.xsasakihaise.hellasforms;

import com.pixelmonmod.pixelmon.items.QuestItem;

/**
 * Generic quest item wrapper used for the many mineral drops stored inside
 * Hellas crates. Exists so Pixelmon handles them as non-stackable quest items.
 */
public class OreItem extends QuestItem {

    public OreItem() {
        super();
    }
}
