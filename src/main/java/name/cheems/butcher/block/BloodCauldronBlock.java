package name.cheems.butcher.block;

import name.cheems.butcher.fluid.ModFluids;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class BloodCauldronBlock extends LeveledCauldronBlock {

    public BloodCauldronBlock(AbstractBlock.Settings settings) {
        super(Biome.Precipitation.NONE, ModCauldronBehaviours.BLOOD_CAULDRON_BEHAVIOR, settings);
    }

    @Override
    protected boolean canBeFilledByDripstone(Fluid fluid) {
        return fluid == ModFluids.STILL_BLOOD;
    }

    @Override
    protected void fillFromDripstone(BlockState state, World world, BlockPos pos, Fluid fluid) {
        if (fluid == ModFluids.STILL_BLOOD) {
            int level = state.get(LeveledCauldronBlock.LEVEL);

            if (level < 3) {
                world.setBlockState(pos, state.with(LeveledCauldronBlock.LEVEL, level + 1), 3);
            }
        }
    }
}