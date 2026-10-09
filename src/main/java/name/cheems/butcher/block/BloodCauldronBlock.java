package name.cheems.butcher.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.world.biome.Biome;

public class BloodCauldronBlock extends LeveledCauldronBlock {

    public BloodCauldronBlock(AbstractBlock.Settings settings) {
        super(Biome.Precipitation.NONE, ModCauldronBehaviours.BLOOD_CAULDRON_BEHAVIOR, settings);
    }
}