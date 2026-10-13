package name.cheems.butcher.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public record MeatGrinderRecipie(Ingredient inputItem, ItemStack output) implements Recipe<MeatGrinderRecipeInput> {

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.inputItem);
        return list;
    }



    @Override
    public boolean matches(MeatGrinderRecipeInput input, World world) {
        if(world.isClient()) {
            return false;
        }

        return inputItem.test(input.getStackInSlot(0));
    }

    @Override
    public ItemStack craft(MeatGrinderRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipies.MEAT_GRINDER_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipies.MEAT_GRINDER_TYPE;
    }

    public static class Serializer implements RecipeSerializer<MeatGrinderRecipie> {
        public static final MapCodec<MeatGrinderRecipie> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(MeatGrinderRecipie::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(MeatGrinderRecipie::output)
        ).apply(inst, MeatGrinderRecipie::new));

        public static final PacketCodec<RegistryByteBuf, MeatGrinderRecipie> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, MeatGrinderRecipie::inputItem,
                        ItemStack.PACKET_CODEC, MeatGrinderRecipie::output,
                        MeatGrinderRecipie::new);

        @Override
        public MapCodec<MeatGrinderRecipie> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, MeatGrinderRecipie> packetCodec() {
            return STREAM_CODEC;
        }
    }
}
