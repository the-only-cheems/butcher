package name.cheems.butcher.block;

import name.cheems.butcher.Butcher;
import name.cheems.butcher.fluid.ModFluids;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;

public class ModCauldronBehaviours {

    public static final CauldronBehavior.CauldronBehaviorMap BLOOD_CAULDRON_BEHAVIOR =
            CauldronBehavior.createMap("blood");

    public static void registerModCauldronBehaviours() {
        Butcher.LOGGER.info("Registering cauldron behaviours for " + Butcher.MOD_ID);

        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(ModFluids.BLOOD_BUCKET,
 (state, world, pos, player, hand, stack) ->
                        CauldronBehavior.fillCauldron(world, pos, player, hand, stack, ModBlocks.BLOOD_CAULDRON_BLOCK.getDefaultState().with(LeveledCauldronBlock.LEVEL, 3), SoundEvents.ITEM_BUCKET_EMPTY));

        BLOOD_CAULDRON_BEHAVIOR.map().put(Items.BUCKET, (state, world, pos, player, hand, stack) ->
                        CauldronBehavior.emptyCauldron(state, world, pos, player, hand, stack, ModFluids.BLOOD_BUCKET.getDefaultStack(), s -> s.get(LeveledCauldronBlock.LEVEL) >= 3, SoundEvents.ITEM_BUCKET_FILL));
    }
}