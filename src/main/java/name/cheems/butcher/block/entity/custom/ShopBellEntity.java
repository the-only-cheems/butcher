package name.cheems.butcher.block.entity.custom;

import name.cheems.butcher.Butcher;
import name.cheems.butcher.block.entity.ModBlockEntities;
import name.cheems.butcher.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

public class ShopBellEntity extends BlockEntity {
    private BlockPos doorPos;

    private boolean wasOpen = false;
    private boolean initialized = false;

    public ShopBellEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHOP_BELL_BE, pos, state);
    }

    public void setDoorPos(BlockPos pos) {
        if (world == null) {
            this.doorPos = pos.toImmutable();
            this.initialized = false;
            return;
        }
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof DoorBlock && state.get(DoorBlock.HALF) == DoubleBlockHalf.UPPER){
            pos = pos.down();
        }

        this.doorPos = pos.toImmutable();
        this.initialized = false;
        markDirty();

    }

    public BlockPos getDoorPos() {
        return doorPos;
    }

    public static void tick(World world, BlockPos pos, BlockState state, ShopBellEntity blockEntity){

        if(world.isClient() || blockEntity.doorPos == null){
            return;
        }

        BlockPos targetPos = blockEntity.doorPos;
        BlockState doorState = world.getBlockState(targetPos);

        if (!(doorState.getBlock() instanceof DoorBlock)) {
            blockEntity.doorPos = null;
            blockEntity.initialized = false;
            blockEntity.markDirty();
            return;
        }

        boolean isOpen = doorState.get(DoorBlock.OPEN);

        if (!blockEntity.initialized){
            blockEntity.wasOpen = isOpen;
            blockEntity.initialized = true;
            return;
        }

        if(isOpen && !blockEntity.wasOpen){
            world.playSound(null, pos, ModSounds.SHOP_DOOR_BELL, SoundCategory.BLOCKS, 1.0f, 1.0f);

        }

        blockEntity.wasOpen = isOpen;
    }

    @Override
    protected void writeNbt(
            NbtCompound nbt,
            RegistryWrapper.WrapperLookup registries
    ) {
        super.writeNbt(nbt, registries);

        if (doorPos != null) {
            nbt.putInt("DoorX", doorPos.getX());
            nbt.putInt("DoorY", doorPos.getY());
            nbt.putInt("DoorZ", doorPos.getZ());
            nbt.putBoolean("HasDoor", true);
        } else {
            nbt.putBoolean("HasDoor", false);
        }

        nbt.putBoolean("WasOpen", wasOpen);
        nbt.putBoolean("Initialized", initialized);
    }

    @Override
    protected void readNbt(
            NbtCompound nbt,
            RegistryWrapper.WrapperLookup registries
    ) {
        super.readNbt(nbt, registries);

        if (nbt.getBoolean("HasDoor")) {
            doorPos = new BlockPos(
                    nbt.getInt("DoorX"),
                    nbt.getInt("DoorY"),
                    nbt.getInt("DoorZ")
            );
        } else {
            doorPos = null;
        }

        wasOpen = nbt.getBoolean("WasOpen");
        initialized = nbt.getBoolean("Initialized");
    }
}