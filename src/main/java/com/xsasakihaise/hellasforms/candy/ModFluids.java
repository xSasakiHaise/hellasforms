package com.xsasakihaise.hellasforms.candy;

import com.xsasakihaise.hellasforms.HellasForms;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers the HellasForms fluid set (source + flowing + block + bucket) used by the EXP and attribute systems.
 */
public final class ModFluids {

    private ModFluids() {
    }

    private static final AbstractBlock.Properties FLUID_BLOCK_PROPERTIES =
            AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops();

    private static final Item.Properties BUCKET_PROPERTIES =
            new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_MISC).craftRemainder(Items.BUCKET);

    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, HellasForms.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, HellasForms.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HellasForms.MOD_ID);

    public static final FluidSet LIQUID_EXP_CANDY = registerFluid("liquid_exp_candy");
    public static final FluidSet LIQUID_STR = registerFluid("liquid_str");
    public static final FluidSet LIQUID_DEX = registerFluid("liquid_dex");
    public static final FluidSet LIQUID_CON = registerFluid("liquid_con");
    public static final FluidSet LIQUID_INT = registerFluid("liquid_int");
    public static final FluidSet LIQUID_WIS = registerFluid("liquid_wis");
    public static final FluidSet LIQUID_CHA = registerFluid("liquid_cha");

    private static FluidSet registerFluid(String name) {
        FluidSet set = new FluidSet(name);
        set.source = FLUIDS.register(name, () -> new ForgeFlowingFluid.Source(set.properties));
        set.flowing = FLUIDS.register("flowing_" + name, () -> new ForgeFlowingFluid.Flowing(set.properties));
        set.block = BLOCKS.register(name, () -> new FlowingFluidBlock(set::getSourceFluid, FLUID_BLOCK_PROPERTIES));
        set.bucket = ITEMS.register(name + "_bucket", () -> new BucketItem(set::getSourceFluid, BUCKET_PROPERTIES));

        ResourceLocation still = new ResourceLocation(HellasForms.MOD_ID, "block/" + name + "_still");
        ResourceLocation flow = new ResourceLocation(HellasForms.MOD_ID, "block/" + name + "_flow");
        set.properties = new ForgeFlowingFluid.Properties(set::getSourceFluid, set::getFlowingFluid,
                FluidAttributes.builder(still, flow))
                .levelDecreasePerBlock(1)
                .slopeFindDistance(4)
                .bucket(set::getBucketItem)
                .block(set::getFluidBlock)
                .explosionResistance(100F);
        return set;
    }

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }

    public static final class FluidSet {
        private final String name;
        private RegistryObject<ForgeFlowingFluid.Source> source;
        private RegistryObject<ForgeFlowingFluid.Flowing> flowing;
        private RegistryObject<FlowingFluidBlock> block;
        private RegistryObject<Item> bucket;
        private ForgeFlowingFluid.Properties properties;

        private FluidSet(String name) {
            this.name = name;
        }

        public ForgeFlowingFluid.Source getSourceFluid() {
            return source.get();
        }

        public ForgeFlowingFluid.Flowing getFlowingFluid() {
            return flowing.get();
        }

        public FlowingFluidBlock getFluidBlock() {
            return block.get();
        }

        public Item getBucketItem() {
            return bucket.get();
        }

        public String getName() {
            return name;
        }
    }
}
