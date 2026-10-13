package name.cheems.butcher.recipe;

import name.cheems.butcher.Butcher;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipies {
    public static final RecipeSerializer<MeatGrinderRecipie> MEAT_GRINDER_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, Identifier.of(Butcher.MOD_ID, "meat_grinder"),
            new MeatGrinderRecipie.Serializer());

    public static final RecipeType<MeatGrinderRecipie> MEAT_GRINDER_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(Butcher.MOD_ID, "meat_grinder"),
            new RecipeType<MeatGrinderRecipie>() {
                @Override
                public String toString() {
                    return "meat_grinder";
                }
            });
    public static void registerRecipes() {
        Butcher.LOGGER.info("Registering recipes for " + Butcher.MOD_ID);
    }

}
