package name.cheems.butcher.fluid;

import name.cheems.butcher.Butcher;
import name.cheems.butcher.block.ModBlocks;
import net.fabricmc.fabric.api.transfer.v1.fluid.CauldronFluidContent;
import net.minecraft.block.*;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModFluids {

    public static FlowableFluid STILL_BLOOD;
    public static FlowableFluid FLOWING_BLOOD;
    public static Block BLOOD_BLOCK;
    public static Item BLOOD_BUCKET;

    public static void registerModFluids() {
        Butcher.LOGGER.info("Registering fluids " + Butcher.MOD_ID);
        STILL_BLOOD = Registry.register(Registries.FLUID, Identifier.of(Butcher.MOD_ID, "blood"), new BloodFluid.Still());
        FLOWING_BLOOD = Registry.register(Registries.FLUID, Identifier.of(Butcher.MOD_ID, "flowing_blood"), new BloodFluid.Flowing());

        BLOOD_BLOCK = Registry.register(Registries.BLOCK, Identifier.of(Butcher.MOD_ID, "blood_block"), new FluidBlock(ModFluids.STILL_BLOOD, AbstractBlock.Settings.copy(Blocks.WATER)));
        BLOOD_BUCKET = Registry.register(Registries.ITEM, Identifier.of(Butcher.MOD_ID, "blood_bucket"), new BucketItem(ModFluids.STILL_BLOOD, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1)));

        CauldronFluidContent.registerCauldron(ModBlocks.BLOOD_CAULDRON_BLOCK, ModFluids.STILL_BLOOD, 333, LeveledCauldronBlock.LEVEL);
    }
}
