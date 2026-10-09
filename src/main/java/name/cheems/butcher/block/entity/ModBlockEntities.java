package name.cheems.butcher.block.entity;

import name.cheems.butcher.Butcher;
import name.cheems.butcher.block.ModBlocks;
import name.cheems.butcher.block.entity.custom.MeatGrinderEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static final BlockEntityType<MeatGrinderEntity> GRINDER_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Butcher.MOD_ID,"grinder_be"),
            BlockEntityType.Builder.create(MeatGrinderEntity::new, ModBlocks.MEAT_GRINDER).build(null));

    public static void registerBlockEntities() {
        Butcher.LOGGER.info("Registering Block entities for " + Butcher.MOD_ID);
    }
}
