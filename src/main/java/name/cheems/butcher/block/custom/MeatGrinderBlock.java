package name.cheems.butcher.block.custom;

import com.mojang.serialization.MapCodec;
import name.cheems.butcher.block.entity.custom.MeatGrinderEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MeatGrinderBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<MeatGrinderBlock> CODEC = MeatGrinderBlock.createCodec(MeatGrinderBlock::new);


    public MeatGrinderBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MeatGrinderEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof MeatGrinderEntity) {
                ItemScatterer.spawn(world, pos, ((MeatGrinderEntity) blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
