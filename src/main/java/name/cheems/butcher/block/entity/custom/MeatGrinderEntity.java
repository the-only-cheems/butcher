package name.cheems.butcher.block.entity.custom;

import name.cheems.butcher.block.custom.MeatGrinderBlock;
import name.cheems.butcher.block.entity.ImplementedInventory;
import name.cheems.butcher.block.entity.ModBlockEntities;
import name.cheems.butcher.fluid.ModFluids;
import name.cheems.butcher.item.ModItems;
import name.cheems.butcher.recipe.MeatGrinderRecipeInput;
import name.cheems.butcher.recipe.MeatGrinderRecipie;
import name.cheems.butcher.recipe.ModRecipies;
import name.cheems.butcher.screen.custom.MeatGrinderScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MeatGrinderEntity extends BlockEntity implements ImplementedInventory, ExtendedScreenHandlerFactory<BlockPos> {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int BUCKET_INPUT_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;
    private static final int BLOOD_OUTPUT_SLOT = 3;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;

    public MeatGrinderEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GRINDER_BE, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> MeatGrinderEntity.this.progress;
                    case 1 -> MeatGrinderEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: MeatGrinderEntity.this.progress = value;
                    case 1: MeatGrinderEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }


    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("meat_grinder.progress", progress);
        nbt.putInt("meat_grinder.max_progress", maxProgress);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup);
        progress = nbt.getInt("meat_grinder.progress");
        maxProgress = nbt.getInt("meat_grinder.max_progress");
        super.readNbt(nbt, registryLookup);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        boolean crafting = hasRecipe();

        if (state.get(MeatGrinderBlock.ACTIVE) != crafting) {
            world.setBlockState(pos, state.with(MeatGrinderBlock.ACTIVE, crafting), Block.NOTIFY_ALL);
        }

        if (crafting) {
            increaseCraftingProgress();
            markDirty(world, pos, state);

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = 72;
    }

    private void addToOutputSlot(int slot, ItemStack output) {
        ItemStack currentStack = getStack(slot);

        if (currentStack.isEmpty()) {
            setStack(slot, output);
        } else {
            currentStack.increment(output.getCount());
        }
    }

    private void craftItem() {
        Optional<RecipeEntry<MeatGrinderRecipie>> recipe =
                getCurrentRecipe();

        if (recipe.isEmpty()) {
            return;
        }

        ItemStack output = recipe.get().value().output();

        removeStack(INPUT_SLOT, 1);
        addToOutputSlot(OUTPUT_SLOT, output.copy());

        if (getStack(BUCKET_INPUT_SLOT).isOf(Items.BUCKET)) {
            ItemStack bloodBucket =
                    new ItemStack(ModFluids.BLOOD_BUCKET);

            removeStack(BUCKET_INPUT_SLOT, 1);
            addToOutputSlot(BLOOD_OUTPUT_SLOT, bloodBucket);
        }
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        this.progress++;
    }

    private boolean hasRecipe() {
        Optional<RecipeEntry<MeatGrinderRecipie>> recipe = getCurrentRecipe();
        if(recipe.isEmpty()){
            return false;
        }

        ItemStack output = recipe.get().value().output();

        if (!canInsertItemIntoSlot(OUTPUT_SLOT, output)
                || !canInsertItemIntoSlot(OUTPUT_SLOT, output)) {
            return false;
        }

        // If an empty bucket is present, Blood Bucket must fit too.
        if (getStack(BUCKET_INPUT_SLOT).isOf(Items.BUCKET)) {
            ItemStack bloodBucket =
                    new ItemStack(ModFluids.BLOOD_BUCKET);

            if (!canInsertItemIntoSlot(BLOOD_OUTPUT_SLOT, bloodBucket)
                    || !canInsertItemIntoSlot(
                    BLOOD_OUTPUT_SLOT, bloodBucket)) {
                return false;
            }
        }

        return true;
    }

    private Optional<RecipeEntry<MeatGrinderRecipie>> getCurrentRecipe() {
        return this.getWorld().getRecipeManager()
                .getFirstMatch(ModRecipies.MEAT_GRINDER_TYPE, new MeatGrinderRecipeInput(inventory.get(INPUT_SLOT)), this.getWorld());
    }

    private boolean canInsertItemIntoSlot(
            int slot, ItemStack output) {

        ItemStack currentStack = getStack(slot);

        return currentStack.isEmpty()
                || ItemStack.areItemsAndComponentsEqual(
                currentStack, output);
    }

    private boolean canInsertAmountIntoSlot(
            int slot, ItemStack output) {

        ItemStack currentStack = getStack(slot);

        int currentCount = currentStack.isEmpty()
                ? 0 : currentStack.getCount();

        int maxCount = currentStack.isEmpty()
                ? output.getMaxCount()
                : currentStack.getMaxCount();

        return currentCount + output.getCount() <= maxCount;
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return this.pos;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.butcher.meat_grinder");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new MeatGrinderScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }


    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}
