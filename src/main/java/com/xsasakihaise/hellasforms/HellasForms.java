package com.xsasakihaise.hellasforms;

import net.minecraftforge.event.RegisterCommandsEvent;
import com.xsasakihaise.hellasforms.commands.FormsVersionCommand;
import com.xsasakihaise.hellasforms.commands.FormsDependenciesCommand;
import com.xsasakihaise.hellasforms.commands.FormsFeaturesCommand;
import battles.attacks.specialAttacks.basic.*;
import battles.attacks.specialAttacks.multiTurn.*;
import battles.status.*;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.attacks.EffectTypeAdapter;
import com.pixelmonmod.pixelmon.init.registry.ItemRegistration;
import com.pixelmonmod.pixelmon.items.BadgeItem;
import com.pixelmonmod.pixelmon.items.QuestItem;
import com.pixelmonmod.pixelmon.items.heldItems.MegaStoneItem;
import com.xsasakihaise.hellasforms.items.consumables.AbilityPatchRemoverItem;
import com.xsasakihaise.hellasforms.items.consumables.EvMaximizerItem;
import com.xsasakihaise.hellasforms.items.consumables.ExperienceCandyItem;
import com.xsasakihaise.hellasforms.items.consumables.RustedBottleCapItem;
import com.xsasakihaise.hellasforms.items.heldItems.EeveeoliteItem;
import com.xsasakihaise.hellasforms.diagnostics.DebuggingHooks;
import com.xsasakihaise.hellasforms.diagnostics.LogFlag;
import com.xsasakihaise.hellasforms.listener.GrowthSpawningListener;
import com.xsasakihaise.hellasforms.listener.ReturnItemsListener;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.xsasakihaise.hellascontrol.api.sidemods.HellasAPIControlForms;
import com.xsasakihaise.hellasforms.HellasFormsInfoConfig;
import com.xsasakihaise.hellasforms.util.PixelmonStatTypes;

@Mod("hellasforms")
public class HellasForms {

    public static final String MOD_ID = "hellasforms";
    public static final Logger LOGGER = LogManager.getLogger("hellasforms");

    static {
        DebuggingHooks.runWithTracing(LogFlag.API, "HellasAPIControlForms.verify()", LOGGER, HellasAPIControlForms::verify);
    }
    private static HellasForms instance;
    public static HellasFormsInfoConfig infoConfig;
    public static final DeferredRegister<Item> ITEMS;

    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "hellasforms");
    }

    public HellasForms() {
        DebuggingHooks.initialize(LOGGER);
        DebuggingHooks.runWithTracing(LogFlag.CORE, "HellasForms::<init>", LOGGER, () -> {
            instance = this;

            DebuggingHooks.runWithTracing(LogFlag.CORE, "registerLifecycleListeners", LOGGER, () -> {
                IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
                modEventBus.addListener(this::setup);
                MinecraftForge.EVENT_BUS.register(this);
                MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
            });

            DebuggingHooks.runWithTracing(LogFlag.CONFIG, "initializeInfoConfig", LOGGER, () -> {
                infoConfig = new HellasFormsInfoConfig();
                infoConfig.loadDefaultsFromResource();
            });

            DebuggingHooks.runWithTracing(LogFlag.BATTLES, "registerBattleEffectAdapters", LOGGER, () -> {
                EffectTypeAdapter.EFFECTS.put("ColonySwarm", ColonySwarm.class);
                EffectTypeAdapter.EFFECTS.put("Corrode", Corrode.class);
                EffectTypeAdapter.EFFECTS.put("GreekFire", GreekFire.class);
                EffectTypeAdapter.EFFECTS.put("HitchKick", HitchKick.class);
                EffectTypeAdapter.EFFECTS.put("PlasmaFangs", PlasmaFangs.class);
                EffectTypeAdapter.EFFECTS.put("RabidClaw", RabidClaw.class);
                EffectTypeAdapter.EFFECTS.put("PlasmaVeil", PlasmaVeil.class);

                EffectTypeAdapter.EFFECTS.put("RadiantEnergy", RadiantEnergy.class);
                EffectTypeAdapter.EFFECTS.put("BleedingJaw", BleedingJaw.class);
                EffectTypeAdapter.EFFECTS.put("Shatterstorm", Shatterstorm.class);
                EffectTypeAdapter.EFFECTS.put("SnowstormFury", SnowstormFury.class);
                EffectTypeAdapter.EFFECTS.put("Afterburner", Afterburner.class);
                EffectTypeAdapter.EFFECTS.put("VenomAmbush", VenomAmbush.class);
                EffectTypeAdapter.EFFECTS.put("DC", DC.class);
                EffectTypeAdapter.EFFECTS.put("PhotonDarts", PhotonDarts.class);
                EffectTypeAdapter.EFFECTS.put("SurgeBurrow", SurgeBurrow.class);

                EffectTypeAdapter.EFFECTS.put("TiroFinale", TiroFinale.class);
                EffectTypeAdapter.EFFECTS.put("AuroraInfernalis", AuroraInfernalis.class);
                EffectTypeAdapter.EFFECTS.put("StingingNettle", StingingNettle.class);
                EffectTypeAdapter.EFFECTS.put("PetalBurst", PetalBurst.class);
                EffectTypeAdapter.EFFECTS.put("Purification", Purification.class);
                EffectTypeAdapter.EFFECTS.put("Thermodynamics", Thermodynamics.class);
                EffectTypeAdapter.EFFECTS.put("IcyImmolation", IcyImmolation.class);
            });

            DebuggingHooks.runWithTracing(LogFlag.ITEMS, "registerPixelmonItems", LOGGER, () -> {
                ItemRegistration.ITEMS.register("eeveeolite", EeveeoliteItem::new);

                ItemRegistration.ITEMS.register("diancite-hellas", () -> new MegaStoneItem("diancie form:hellas"));
                ItemRegistration.ITEMS.register("sceptilite-hellas", () -> new MegaStoneItem("sceptile form:hellas"));
                ItemRegistration.ITEMS.register("gyaradosite-hellas", () -> new MegaStoneItem("gyarados form:hellas"));
                ItemRegistration.ITEMS.register("tyranitarite-hellas", () -> new MegaStoneItem("tyranitar form:hellas"));
                ItemRegistration.ITEMS.register("gardevoirite-hellas", () -> new MegaStoneItem("gardevoir form:hellas"));
                ItemRegistration.ITEMS.register("garchompite-hellas", () -> new MegaStoneItem("garchomp form:hellas"));
                ItemRegistration.ITEMS.register("galladeite-hellas", () -> new MegaStoneItem("gallade form:hellas"));
                ItemRegistration.ITEMS.register("samurottite-hellas", () -> new MegaStoneItem("samurott form:hellas"));
                ItemRegistration.ITEMS.register("centiskorchite-hellas", () -> new MegaStoneItem("centiskorch form:hellas"));
                ItemRegistration.ITEMS.register("aegislashite-hellas", () -> new MegaStoneItem("aegislash form:hellas"));
                ItemRegistration.ITEMS.register("goodrite-hellas", () -> new MegaStoneItem("goodra form:hellas"));

                ItemRegistration.ITEMS.register("bug_badge_hellas", BadgeItem::new);
                ItemRegistration.ITEMS.register("dark_badge_hellas", BadgeItem::new);
                ItemRegistration.ITEMS.register("dragon_badge_hellas", BadgeItem::new);
                ItemRegistration.ITEMS.register("electric_badge_hellas", BadgeItem::new);
                ItemRegistration.ITEMS.register("fairy_badge_hellas", BadgeItem::new);
                ItemRegistration.ITEMS.register("fighting_badge_hellas", BadgeItem::new);
                ItemRegistration.ITEMS.register("fire_badge_hellas", BadgeItem::new);
                ItemRegistration.ITEMS.register("flying_badge_hellas", BadgeItem::new);
                ItemRegistration.ITEMS.register("ghost_badge_hellas", BadgeItem::new);
                ItemRegistration.ITEMS.register("grass_badge_hellas", BadgeItem::new);
                ItemRegistration.ITEMS.register("ground_badge_hellas", BadgeItem::new);
                ItemRegistration.ITEMS.register("ice_badge_hellas", BadgeItem::new);
                ItemRegistration.ITEMS.register("normal_badge_hellas", BadgeItem::new);
                ItemRegistration.ITEMS.register("poison_badge_hellas", BadgeItem::new);
                ItemRegistration.ITEMS.register("psychic_badge_hellas", BadgeItem::new);
                ItemRegistration.ITEMS.register("rock_badge_hellas", BadgeItem::new);
                ItemRegistration.ITEMS.register("steel_badge_hellas", BadgeItem::new);
                ItemRegistration.ITEMS.register("water_badge_hellas", BadgeItem::new);

                ItemRegistration.ITEMS.register("rusted_bottle_cap", RustedBottleCapItem::new);
                ItemRegistration.ITEMS.register("exp_candy_xxl", () -> new ExperienceCandyItem(60000, "item.pixelmon.exp_candy_xxl.success"));
                ItemRegistration.ITEMS.register("exp_candy_xxxl", () -> new ExperienceCandyItem(120000, "item.pixelmon.exp_candy_xxxl.success"));
                ItemRegistration.ITEMS.register("hp_252_ev_capsule", () -> new EvMaximizerItem(PixelmonStatTypes.hp(), "item.pixelmon.hp_252_ev_capsule.success"));
                ItemRegistration.ITEMS.register("attack_252_ev_capsule", () -> new EvMaximizerItem(PixelmonStatTypes.attack(), "item.pixelmon.attack_252_ev_capsule.success"));
                ItemRegistration.ITEMS.register("defense_252_ev_capsule", () -> new EvMaximizerItem(PixelmonStatTypes.defence(), "item.pixelmon.defense_252_ev_capsule.success"));
                ItemRegistration.ITEMS.register("spatk_252_ev_capsule", () -> new EvMaximizerItem(PixelmonStatTypes.specialAttack(), "item.pixelmon.spatk_252_ev_capsule.success"));
                ItemRegistration.ITEMS.register("spdef_252_ev_capsule", () -> new EvMaximizerItem(PixelmonStatTypes.specialDefence(), "item.pixelmon.spdef_252_ev_capsule.success"));
                ItemRegistration.ITEMS.register("speed_252_ev_capsule", () -> new EvMaximizerItem(PixelmonStatTypes.speed(), "item.pixelmon.speed_252_ev_capsule.success"));
                ItemRegistration.ITEMS.register("ability_patch_remover", AbilityPatchRemoverItem::new);
                ItemRegistration.ITEMS.register("hellas_coupon", QuestItem::new);
                ItemRegistration.ITEMS.register("deck-of-many-mons", QuestItem::new);
            });

            DebuggingHooks.runWithTracing(LogFlag.ITEMS, "registerHellasItems", LOGGER, () -> {
                ITEMS.register("wild-egg", PokemonEggItem::new);
                ITEMS.register("hellasian-egg", PokemonEggItem::new);
                ITEMS.register("odd-looking-egg", PokemonEggItem::new);
                ITEMS.register("sparkly-wild-egg", PokemonEggItem::new);
                ITEMS.register("sparkly-hellasian-egg", PokemonEggItem::new);
                ITEMS.register("sparkly-odd-looking-egg", PokemonEggItem::new);

                for (int i = 1; i <= 20; i++) {
                    ITEMS.register("battlepass_item_" + i, BattlePassItem::new);
                }

                ITEMS.register("hellas_adamantite", OreItem::new);
                ITEMS.register("hellas_amazonite", OreItem::new);
                ITEMS.register("hellas_amber", OreItem::new);
                ITEMS.register("hellas_amethyst", OreItem::new);
                ITEMS.register("hellas_ametrine", OreItem::new);
                ITEMS.register("hellas_apatite", OreItem::new);
                ITEMS.register("hellas_aquamarine", OreItem::new);
                ITEMS.register("hellas_aventurine", OreItem::new);
                ITEMS.register("hellas_azurite", OreItem::new);
                ITEMS.register("hellas_blue_flourite", OreItem::new);
                ITEMS.register("hellas_blue_quartz", OreItem::new);
                ITEMS.register("hellas_blue_topaz", OreItem::new);
                ITEMS.register("hellas_carnelian", OreItem::new);
                ITEMS.register("hellas_chromite", OreItem::new);
                ITEMS.register("hellas_chrysoberyl", OreItem::new);
                ITEMS.register("hellas_cinnabar", OreItem::new);
                ITEMS.register("hellas_citrine", OreItem::new);
                ITEMS.register("hellas_diamond", OreItem::new);
                ITEMS.register("hellas_dolomite", OreItem::new);
                ITEMS.register("hellas_emerald", OreItem::new);
                ITEMS.register("hellas_erythrite", OreItem::new);
                ITEMS.register("hellas_flourite", OreItem::new);
                ITEMS.register("hellas_fuchsite", OreItem::new);
                ITEMS.register("hellas_garnet", OreItem::new);
                ITEMS.register("hellas_green_flourite", OreItem::new);
                ITEMS.register("hellas_hexagonite", OreItem::new);
                ITEMS.register("hellas_jade", OreItem::new);
                ITEMS.register("hellas_jasper", OreItem::new);
                ITEMS.register("hellas_lapis_lazuli", OreItem::new);
                ITEMS.register("hellas_lazulite", OreItem::new);
                ITEMS.register("hellas_lazurite", OreItem::new);
                ITEMS.register("hellas_malachite", OreItem::new);
                ITEMS.register("hellas_mithril", OreItem::new);
                ITEMS.register("hellas_morganite", OreItem::new);
                ITEMS.register("hellas_moss_agate", OreItem::new);
                ITEMS.register("hellas_nephrite", OreItem::new);
                ITEMS.register("hellas_neptunite", OreItem::new);
                ITEMS.register("hellas_obsidian", OreItem::new);
                ITEMS.register("hellas_onyx", OreItem::new);
                ITEMS.register("hellas_opal", OreItem::new);
                ITEMS.register("hellas_orange_flourite", OreItem::new);
                ITEMS.register("hellas_peridot", OreItem::new);
                ITEMS.register("hellas_peridotite", OreItem::new);
                ITEMS.register("hellas_phosgenite", OreItem::new);
                ITEMS.register("hellas_phosphophyllite", OreItem::new);
                ITEMS.register("hellas_phosphosiderite", OreItem::new);
                ITEMS.register("hellas_pink_ruby", OreItem::new);
                ITEMS.register("hellas_pink_topaz", OreItem::new);
                ITEMS.register("hellas_pupurite", OreItem::new);
                ITEMS.register("hellas_purple_flourite", OreItem::new);
                ITEMS.register("hellas_pyrargyrite", OreItem::new);
                ITEMS.register("hellas_pyrite", OreItem::new);
                ITEMS.register("hellas_pyroxmagnite", OreItem::new);
                ITEMS.register("hellas_quartz", OreItem::new);
                ITEMS.register("hellas_red_flourite", OreItem::new);
                ITEMS.register("hellas_rose_quartz", OreItem::new);
                ITEMS.register("hellas_ruby", OreItem::new);
                ITEMS.register("hellas_sapphire", OreItem::new);
                ITEMS.register("hellas_sapphirine", OreItem::new);
                ITEMS.register("hellas_smoky_quartz", OreItem::new);
                ITEMS.register("hellas_soapstone", OreItem::new);
                ITEMS.register("hellas_sulfur", OreItem::new);
                ITEMS.register("hellas_tanzanite", OreItem::new);
                ITEMS.register("hellas_titanite", OreItem::new);
                ITEMS.register("hellas_topaz", OreItem::new);
                ITEMS.register("hellas_turquoise", OreItem::new);
                ITEMS.register("hellas_yellow_flourite", OreItem::new);

                ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
            });
        });
    }

    private void setup(FMLCommonSetupEvent event) {
        DebuggingHooks.runWithTracing(LogFlag.CORE, "setup(FMLCommonSetupEvent)", LOGGER, () ->
                LOGGER.info("{} Loaded HellasForms (hopefully XD)", LogFlag.CORE.format()));
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        DebuggingHooks.runWithTracing(LogFlag.CORE, "onServerStarting(FMLServerStartingEvent)", LOGGER, () -> {
            DebuggingHooks.runWithTracing(LogFlag.CONFIG, "HellasFormsInfoConfig.load", LOGGER, () ->
                    infoConfig.load(event.getServer().getServerDirectory()));
            DebuggingHooks.runWithTracing(LogFlag.LISTENERS, "registerReturnItemsListener", LOGGER, () ->
                    Pixelmon.EVENT_BUS.register(new ReturnItemsListener()));
            DebuggingHooks.runWithTracing(LogFlag.LISTENERS, "registerGrowthSpawningListener", LOGGER, () ->
                    MinecraftForge.EVENT_BUS.register(new GrowthSpawningListener()));
        });
    }

    @SubscribeEvent
    public static void onServerStarted(FMLServerStartedEvent event) { }

    @SubscribeEvent
    public static void onServerStopping(FMLServerStoppingEvent event) { }

    @SubscribeEvent
    public static void onServerStopped(FMLServerStoppedEvent event) { }

    public static HellasForms getInstance() { return instance; }

    public static Logger getLogger() { return LOGGER; }

    public void ModEventSubscriber() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        DebuggingHooks.runWithTracing(LogFlag.CORE, "commonSetup(FMLCommonSetupEvent)", LOGGER, () -> {
            // Intentionally blank hook for future use
        });
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        DebuggingHooks.runWithTracing(LogFlag.CORE, "clientSetup(FMLClientSetupEvent)", LOGGER, () -> {
            // Intentionally blank hook for future use
        });
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        DebuggingHooks.runWithTracing(LogFlag.CORE, "onRegisterCommands", LOGGER, () -> {
            DebuggingHooks.runWithTracing(LogFlag.COMMANDS, "FormsVersionCommand.register", LOGGER, () ->
                    FormsVersionCommand.register(event.getDispatcher(), infoConfig));
            DebuggingHooks.runWithTracing(LogFlag.COMMANDS, "FormsDependenciesCommand.register", LOGGER, () ->
                    FormsDependenciesCommand.register(event.getDispatcher(), infoConfig));
            DebuggingHooks.runWithTracing(LogFlag.COMMANDS, "FormsFeaturesCommand.register", LOGGER, () ->
                    FormsFeaturesCommand.register(event.getDispatcher(), infoConfig));
        });
    }
}
