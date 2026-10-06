package name.cheems.butcher.datagen;

import name.cheems.butcher.block.ModBlocks;
import name.cheems.butcher.fluid.ModFluids;
import name.cheems.butcher.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.FLESH_BLOCK);

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.MYSTERY_MEAT, Models.GENERATED);
        itemModelGenerator.register(ModItems.MYSTERY_MEAT_COOKED, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAZOR, Models.HANDHELD);
        itemModelGenerator.register(ModItems.CLEAVER, Models.HANDHELD);
        itemModelGenerator.register(ModFluids.BLOOD_BUCKET, Models.GENERATED);

    }
}
