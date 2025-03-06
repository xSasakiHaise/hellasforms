package com.xsasakihaise.hellasforms.listener;

import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

public class GrowthSpawningListener {
    public GrowthSpawningListener() {
    }

    @SubscribeEvent
    public void onServerStarted(FMLServerStartedEvent event) {
        EnumGrowth.Microscopic.rarity = 4;
        EnumGrowth.Pygmy.rarity = 4;
        EnumGrowth.Runt.rarity = 10;
        EnumGrowth.Small.rarity = 18;
        EnumGrowth.Ordinary.rarity = 28;
        EnumGrowth.Huge.rarity = 18;
        EnumGrowth.Giant.rarity = 10;
        EnumGrowth.Enormous.rarity = 4;
        EnumGrowth.Ginormous.rarity = 4;
    }
}
