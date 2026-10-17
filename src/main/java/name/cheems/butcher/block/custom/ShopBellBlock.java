package name.cheems.butcher.block.custom;

import com.mojang.serialization.MapCodec;
import name.cheems.butcher.block.entity.ModBlockEntities;
import name.cheems.butcher.block.entity.custom.ShopBellEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ShopBellBlock extends BlockWithEntity implements BlockEntityProvider{
    public static final MapCodec<ShopBellBlock> CODEC = ShopBellBlock.createCodec(ShopBellBlock::new);

    private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(13, 13, 0, 16, 16, 3.5);

    private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(12.5, 13, 13, 16, 16, 16);

    private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0, 13, 12.5, 3, 16, 16);

    private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0, 13, 0, 3.5, 16, 3);

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    public ShopBellBlock(Settings settings) {
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
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        if (ctx.getSide().getAxis().isVertical()) {
            return null;
        }

        if (ctx.getSide().getAxis().isHorizontal()){
           return getDefaultState().with(FACING, ctx.getSide().getOpposite());
        }

        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
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
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (world.isClient){
            return;
        }

        Direction facing = state.get(FACING);
        BlockPos doorPos = pos.offset(facing);

        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof ShopBellEntity bell){
            bell.setDoorPos(doorPos);
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ShopBellEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if(world.isClient()) {
            return null;
        }

        return validateTicker(type, ModBlockEntities.SHOP_BELL_BE, ShopBellEntity::tick);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);

        BlockPos attachedPos = pos.offset(state.get(FACING));

        if(world.getBlockState(attachedPos).isAir()){
            world.breakBlock(pos,true);
        }
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
