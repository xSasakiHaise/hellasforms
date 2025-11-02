package com.xsasakihaise.hellasforms.candy;

import com.xsasakihaise.hellasforms.HellasForms;
import com.xsasakihaise.hellasforms.items.consumables.AbilityPatchRemoverItem;
import com.xsasakihaise.hellasforms.items.consumables.ExperienceCandyItem;
import com.xsasakihaise.hellasforms.items.consumables.RustedBottleCapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.RegistryObject;

/**
 * Registers the HellasForms candies, casts, pellets, capsules and related crafting items.
 */
public final class ModItems {

    private ModItems() {
    }

    private static final Item.Properties DEFAULT_PROPS = new Item.Properties().tab(ItemGroup.TAB_MISC);

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HellasForms.MOD_ID);

    public static final RegistryObject<Item> CANDY_CAST_XS = ITEMS.register("candy_cast_xs", () -> new Item(DEFAULT_PROPS));
    public static final RegistryObject<Item> CANDY_CAST_S = ITEMS.register("candy_cast_s", () -> new Item(DEFAULT_PROPS));
    public static final RegistryObject<Item> CANDY_CAST_M = ITEMS.register("candy_cast_m", () -> new Item(DEFAULT_PROPS));
    public static final RegistryObject<Item> CANDY_CAST_L = ITEMS.register("candy_cast_l", () -> new Item(DEFAULT_PROPS));
    public static final RegistryObject<Item> CANDY_CAST_XL = ITEMS.register("candy_cast_xl", () -> new Item(DEFAULT_PROPS));
    public static final RegistryObject<Item> CANDY_CAST_XXL = ITEMS.register("candy_cast_xxl", () -> new Item(DEFAULT_PROPS));
    public static final RegistryObject<Item> CANDY_CAST_XXXL = ITEMS.register("candy_cast_xxxl", () -> new Item(DEFAULT_PROPS));

    public static final RegistryObject<Item> PELLET_CAST = ITEMS.register("pellet_cast", () -> new Item(DEFAULT_PROPS));

    public static final RegistryObject<Item> EXP_CANDY_XXL = ITEMS.register("exp_candy_xxl",
            () -> new ExperienceCandyItem(100_000, "item.hellasforms.exp_candy_xxl.success"));
    public static final RegistryObject<Item> EXP_CANDY_XXXL = ITEMS.register("exp_candy_xxxl",
            () -> new ExperienceCandyItem(350_000, "item.hellasforms.exp_candy_xxxl.success"));

    public static final RegistryObject<Item> PELLET_STR = ITEMS.register("pellet_str", () -> new Item(DEFAULT_PROPS));
    public static final RegistryObject<Item> PELLET_DEX = ITEMS.register("pellet_dex", () -> new Item(DEFAULT_PROPS));
    public static final RegistryObject<Item> PELLET_CON = ITEMS.register("pellet_con", () -> new Item(DEFAULT_PROPS));
    public static final RegistryObject<Item> PELLET_INT = ITEMS.register("pellet_int", () -> new Item(DEFAULT_PROPS));
    public static final RegistryObject<Item> PELLET_WIS = ITEMS.register("pellet_wis", () -> new Item(DEFAULT_PROPS));
    public static final RegistryObject<Item> PELLET_CHA = ITEMS.register("pellet_cha", () -> new Item(DEFAULT_PROPS));

    public static final RegistryObject<Item> CAPSULE_252 = ITEMS.register("capsule_252", () -> new Item(DEFAULT_PROPS));

    public static final RegistryObject<Item> ABILITY_PATCH_REMOVER = ITEMS.register("ability_patch_remover",
            AbilityPatchRemoverItem::new);
    public static final RegistryObject<Item> RUSTED_BOTTLE_CAP = ITEMS.register("rusted_bottle_cap",
            RustedBottleCapItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
