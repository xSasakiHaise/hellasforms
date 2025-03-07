package com.xsasakihaise.hellasforms;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.attacks.EffectTypeAdapter;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.init.registry.ItemRegistration;
import com.pixelmonmod.pixelmon.items.heldItems.MegaStoneItem;
import com.xsasakihaise.hellasforms.battles.attacks.specialAttacks.basic.Corrode;
import com.xsasakihaise.hellasforms.battles.attacks.specialAttacks.basic.HitchKick;
import com.xsasakihaise.hellasforms.battles.attacks.specialAttacks.basic.PlasmaFangs;
import com.xsasakihaise.hellasforms.battles.status.PlasmaVeil;
import com.xsasakihaise.hellasforms.items.heldItems.EeveeoliteItem;
import com.xsasakihaise.hellasforms.listener.GrowthSpawningListener;
import com.xsasakihaise.hellasforms.listener.ReturnItemsListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod("hellasforms")
    @EventBusSubscriber(
            modid = "hellasforms"
    )
    public class HellasForms {
        public static final String MOD_ID = "hellasforms";
        public static final Logger LOGGER = LogManager.getLogger("hellasforms");
        private static HellasForms instance;
        public static final DeferredRegister<Item> ITEMS = null;

        public HellasForms() {
            instance = this;
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
            modEventBus.addListener(this::setup);
            MinecraftForge.EVENT_BUS.register(this);
            EffectTypeAdapter.EFFECTS.put("Corrode", Corrode.class);
            EffectTypeAdapter.EFFECTS.put("PlasmaVeil", PlasmaVeil.class);
            EffectTypeAdapter.EFFECTS.put("HitchKick", HitchKick.class);
            EffectTypeAdapter.EFFECTS.put("PlasmaFangs", PlasmaFangs.class);
            ItemRegistration.ITEMS.register("eeveeolite", EeveeoliteItem::new);
//            ItemRegistration.ITEMS.register("diancite-hellas", MegaStoneItem::new);
//            ItemRegistration.ITEMS.register("sceptilite-hellas", MegaStoneItem::new);
            ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        private void setup(FMLCommonSetupEvent event) {
            LOGGER.info("Loaded HellasForms (hopefully XD)");
        }

        @SubscribeEvent
        public static void onServerStarting(FMLServerStartingEvent event) {
            Pixelmon.EVENT_BUS.register(new ReturnItemsListener());
            MinecraftForge.EVENT_BUS.register(new GrowthSpawningListener());
        }

        @SubscribeEvent
        public static void onServerStarted(FMLServerStartedEvent event) {
        }

        @SubscribeEvent
        public static void onServerStopping(FMLServerStoppingEvent event) {
        }

        @SubscribeEvent
        public static void onServerStopped(FMLServerStoppedEvent event) {
        }

        public static HellasForms getInstance() {
            return instance;
        }

        public static Logger getLogger() {
            return LOGGER;
        }

        public void ModEventSubscriber() {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        }

        @SubscribeEvent
        public void commonSetup(FMLCommonSetupEvent event) {
        }

        @SubscribeEvent
        public void clientSetup(FMLClientSetupEvent event) {
        }

    }
