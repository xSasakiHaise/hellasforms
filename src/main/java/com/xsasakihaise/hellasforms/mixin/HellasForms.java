package com.xsasakihaise.hellasforms.mixin;

import battles.attacks.specialAttacks.basic.*;
import battles.status.*;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.attacks.EffectTypeAdapter;
import com.pixelmonmod.pixelmon.init.registry.ItemRegistration;
import com.pixelmonmod.pixelmon.items.BadgeItem;
import com.pixelmonmod.pixelmon.items.QuestItem;
import com.pixelmonmod.pixelmon.items.heldItems.MegaStoneItem;
import com.xsasakihaise.hellasforms.api.pokemon.ability.abilities.*;
import com.xsasakihaise.hellasforms.items.heldItems.EeveeoliteItem;
import com.xsasakihaise.hellasforms.listener.GrowthSpawningListener;
import com.xsasakihaise.hellasforms.listener.ReturnItemsListener;
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
import net.minecraftforge.registries.ForgeRegistries;
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
    public static final DeferredRegister<Item> ITEMS;

    public HellasForms() {
        instance = this;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
        EffectTypeAdapter.EFFECTS.put("ColonySwarm", ColonySwarm.class);
        EffectTypeAdapter.EFFECTS.put("Corrode", Corrode.class);
        EffectTypeAdapter.EFFECTS.put("GreekFire", GreekFire.class);
        EffectTypeAdapter.EFFECTS.put("HitchKick", HitchKick.class);
        EffectTypeAdapter.EFFECTS.put("PlasmaFangs", PlasmaFangs.class);
        EffectTypeAdapter.EFFECTS.put("RabidClaw", RabidClaw.class);
        EffectTypeAdapter.EFFECTS.put("PlasmaVeil", PlasmaVeil.class);
        ItemRegistration.ITEMS.register("eeveeolite", EeveeoliteItem::new);
        ItemRegistration.ITEMS.register("diancite-hellas", () -> new MegaStoneItem("diancie form:hellas"));
        ItemRegistration.ITEMS.register("sceptilite-hellas", () -> new MegaStoneItem("sceptile form:hellas"));
        ItemRegistration.ITEMS.register("gyaradosite-hellas", () -> new MegaStoneItem("gyarados form:hellas"));
        ItemRegistration.ITEMS.register("wild-egg", QuestItem::new);
        ItemRegistration.ITEMS.register("hellasian-egg", QuestItem::new);
        ItemRegistration.ITEMS.register("odd-looking-egg", QuestItem::new);
        ItemRegistration.ITEMS.register("sparkly-wild-egg", QuestItem::new);
        ItemRegistration.ITEMS.register("sparkly-hellasian-egg", QuestItem::new);
        ItemRegistration.ITEMS.register("sparkly-odd-looking-egg", QuestItem::new);
        ItemRegistration.ITEMS.register("bug_badge", BadgeItem::new);
        ItemRegistration.ITEMS.register("dark_badge", BadgeItem::new);
        ItemRegistration.ITEMS.register("dragon_badge", BadgeItem::new);
        ItemRegistration.ITEMS.register("electric_badge", BadgeItem::new);
        ItemRegistration.ITEMS.register("fairy_badge", BadgeItem::new);
        ItemRegistration.ITEMS.register("fighting_badge", BadgeItem::new);
        ItemRegistration.ITEMS.register("fire_badge", BadgeItem::new);
        ItemRegistration.ITEMS.register("flying_badge", BadgeItem::new);
        ItemRegistration.ITEMS.register("ghost_badge", BadgeItem::new);
        ItemRegistration.ITEMS.register("grass_badge", BadgeItem::new);
        ItemRegistration.ITEMS.register("ground_badge", BadgeItem::new);
        ItemRegistration.ITEMS.register("ice_badge", BadgeItem::new);
        ItemRegistration.ITEMS.register("normal_badge", BadgeItem::new);
        ItemRegistration.ITEMS.register("poison_badge", BadgeItem::new);
        ItemRegistration.ITEMS.register("psychic_badge", BadgeItem::new);
        ItemRegistration.ITEMS.register("rock_badge", BadgeItem::new);
        ItemRegistration.ITEMS.register("steel_badge", BadgeItem::new);
        ItemRegistration.ITEMS.register("water_badge", BadgeItem::new);
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

    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "hellasforms");
    }
}
