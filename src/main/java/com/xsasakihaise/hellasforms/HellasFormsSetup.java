package com.xsasakihaise.hellasforms;

import com.pixelmonmod.pixelmon.api.interactions.InteractionRegistry;
import com.xsasakihaise.hellasforms.interactions.InteractionBottleCap;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = HellasForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class HellasFormsSetup {
    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent e) {
        e.enqueueWork(() -> InteractionRegistry.register(new InteractionBottleCap()));
    }
}
