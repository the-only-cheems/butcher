package name.cheems.butcher.datagen;

import name.cheems.butcher.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.FLESH_BLOCK);
        addDrop(ModBlocks.MEAT_PORTAL, dropsWithSilkTouch(ModBlocks.MEAT_PORTAL));
        addDrop(ModBlocks.FLESH_LAYER);

    }
}
