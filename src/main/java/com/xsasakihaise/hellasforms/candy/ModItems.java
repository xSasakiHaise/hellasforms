package com.xsasakihaise.hellasforms.candy;

import com.xsasakihaise.hellasforms.HellasForms;
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

    public static final RegistryObject<Item> PELLET_STR = ITEMS.register("pellet_str", () -> new Item(DEFAULT_PROPS));
    public static final RegistryObject<Item> PELLET_DEX = ITEMS.register("pellet_dex", () -> new Item(DEFAULT_PROPS));
    public static final RegistryObject<Item> PELLET_CON = ITEMS.register("pellet_con", () -> new Item(DEFAULT_PROPS));
    public static final RegistryObject<Item> PELLET_INT = ITEMS.register("pellet_int", () -> new Item(DEFAULT_PROPS));
    public static final RegistryObject<Item> PELLET_WIS = ITEMS.register("pellet_wis", () -> new Item(DEFAULT_PROPS));
    public static final RegistryObject<Item> PELLET_CHA = ITEMS.register("pellet_cha", () -> new Item(DEFAULT_PROPS));

    /**
     * Hooks every deferred register up to the provided mod event bus. Must be
     * invoked during mod construction.
     */
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
