package name.cheems.butcher.block;

import com.mojang.serialization.MapCodec;
import name.cheems.butcher.block.custom.FleshLayerBlock;
import name.cheems.butcher.block.custom.MeatPortalBlock;
import name.cheems.butcher.sound.ModSounds;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import name.cheems.butcher.Butcher;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block FLESH_BLOCK = registerBlock("flesh_block",
            new FallingBlock(AbstractBlock.Settings.create().strength(0.5f).sounds(ModSounds.FLESH_BLOCK_SOUNDS)) {
                @Override
                protected MapCodec<? extends FallingBlock> getCodec() {
                    return null;
                }
            });

    public static final Block MEAT_PORTAL = registerBlock("meat_portal",
            new MeatPortalBlock(AbstractBlock.Settings.create().strength(1f).requiresTool().sounds(ModSounds.FLESH_BLOCK_SOUNDS)));

    public static final Block FLESH_LAYER = registerBlock("flesh_layer",
            new FleshLayerBlock(AbstractBlock.Settings.create().strength(0.5f).sounds(ModSounds.FLESH_BLOCK_SOUNDS)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Butcher.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Butcher.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        Butcher.LOGGER.info("Registering blocks for "+ Butcher.MOD_ID);
    }
}
