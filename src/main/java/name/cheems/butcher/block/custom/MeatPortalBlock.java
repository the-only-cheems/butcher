package name.cheems.butcher.block.custom;

import name.cheems.butcher.CorruptionManager;
import name.cheems.butcher.teleport.MeatPortalTeleport;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.world.World;

import java.util.Objects;

public class MeatPortalBlock extends Block {

    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    private static final int COOLDOWN_TICKS = 200;

    private static final int SEARCH_RADIUS = 25;

    public MeatPortalBlock(Settings settings) {
        super(settings);

        this.setDefaultState(this.stateManager.getDefaultState()
                .with(ACTIVE, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, net.minecraft.entity.LivingEntity placer, net.minecraft.item.ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (!world.isClient()) {
            world.scheduleBlockTick(pos, this, COOLDOWN_TICKS);
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
    {
        BlockPos otherPortal = findOtherPortal(world, pos);

        if (otherPortal != null) {

            world.setBlockState(pos, state.with(ACTIVE, true));

            BlockState otherState = world.getBlockState(otherPortal);

            if (otherState.isOf(this)) {world.setBlockState(otherPortal, otherState.with(ACTIVE, true));
            }
        }
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity)
    {
        super.onSteppedOn(world, pos, state, entity);

        if (world.isClient()) {
            return;
        }

        if (!state.get(ACTIVE)) {
            return;
        }

        if (!(entity instanceof ServerPlayerEntity player)) {
            return;
        }

        CorruptionManager corruption = CorruptionManager.get(Objects.requireNonNull(entity.getServer()));

        int stage = corruption.getStage(entity.getUuid());

        //This shouts at you, but it is wrong, without this it puts it in chat and looks shit
        ServerPlayerEntity playerEntity = (ServerPlayerEntity) entity;

        if (stage != 3) {
            playerEntity.sendMessage(net.minecraft.text.Text.literal("You are not corrupted enough to enter."), true);
            return;
        }

        BlockPos otherPortal = findOtherPortal(world, pos);

        if (otherPortal == null) {
            world.setBlockState(pos, state.with(ACTIVE, false));
            return;
        }

        MeatPortalTeleport.start(player, pos, otherPortal, corruption);
        world.setBlockState(pos, state.with(ACTIVE, false));

        BlockState otherState = world.getBlockState(otherPortal);

        if (otherState.isOf(this)) {
            world.setBlockState(otherPortal, otherState.with(ACTIVE, false));
        }
        world.scheduleBlockTick(pos, this, COOLDOWN_TICKS);

        world.scheduleBlockTick(otherPortal, this, COOLDOWN_TICKS);
    }


    private BlockPos findOtherPortal(World world, BlockPos pos) {

        for (BlockPos checkPos : BlockPos.iterateOutwards(pos, SEARCH_RADIUS, SEARCH_RADIUS, SEARCH_RADIUS)) {

            //IMPORTANT DON'T DELETE
            if (checkPos.equals(pos)) {
                continue;
            }

            BlockState checkState = world.getBlockState(checkPos);

            if (checkState.isOf(this)) {
                return checkPos.toImmutable();
            }
        }

        return null;
    }
}
