package name.cheems.butcher.block.custom;

import com.mojang.serialization.MapCodec;
import name.cheems.butcher.block.entity.custom.ToolRackEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ToolRackBlock extends BlockWithEntity implements BlockEntityProvider {

    private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0, 14, 14, 16, 16, 16);

    private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0, 14, 0, 2, 16, 16);

    private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0, 14, 0, 16, 16, 2);

    private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(14, 14, 0, 16, 16, 16);

    public static final MapCodec<ToolRackBlock> CODEC = ToolRackBlock.createCodec(ToolRackBlock::new);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public ToolRackBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.with(FACING, mirror.apply(state.get(FACING)));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ToolRackEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof ToolRackEntity) {
                ItemScatterer.spawn(world, pos, ((ToolRackEntity) blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof ToolRackEntity toolRackEntity)) {
            return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!stack.isEmpty()) {
            for (int i = 0; i < toolRackEntity.size(); i++) {
                if (toolRackEntity.getStack(i).isEmpty()) {
                    if (!world.isClient) {
                        toolRackEntity.setStack(i, stack.copyWithCount(1));
                        stack.decrement(1);

                        world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 2f);

                        toolRackEntity.sync();
                    }

                    return ItemActionResult.SUCCESS;
                }
            }

            return ItemActionResult.SUCCESS;
        }

        if (!player.isSneaking()) {
            for (int i = 0; i < toolRackEntity.size(); i++) {
                if (!toolRackEntity.getStack(i).isEmpty()) {
                    if (!world.isClient) {
                        ItemStack storedStack = toolRackEntity.getStack(i).copy();

                        toolRackEntity.setStack(i, ItemStack.EMPTY);

                        player.setStackInHand(hand, storedStack);

                        toolRackEntity.sync();

                        world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
                    }

                    return ItemActionResult.SUCCESS;
                }
            }
        }

        return ItemActionResult.SUCCESS;
    }
}