package name.cheems.butcher.item.custom;

import name.cheems.butcher.Butcher;
import name.cheems.butcher.CorruptionManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class CorruptedFoodItem extends Item {
    private final int corruptionAmount;

    public CorruptedFoodItem(Settings settings, int corruptionAmount) {
        super(settings);
        this.corruptionAmount = corruptionAmount;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        FoodComponent food = stack.get(DataComponentTypes.FOOD);

        if (world.isClient || !(user instanceof ServerPlayerEntity player) || food == null) {
            return super.finishUsing(stack, world, user);
        }

        CorruptionManager manager = CorruptionManager.get(player.getServer());

        int stage = manager.getStage(player.getUuid());

        float nutritionMultiplier = 1.0f;
        float saturationMultiplier = 1.0f;

        switch (stage) {
            case 1 -> {
                nutritionMultiplier = 1.25f;
                saturationMultiplier = 1.25f;
            }
            case 2 -> {
                nutritionMultiplier = 1.5f;
                saturationMultiplier = 1.5f;
            }
            case 3 -> {
                nutritionMultiplier = 2.0f;
                saturationMultiplier = 2.0f;
            }
        }

        int originalNutrition = food.nutrition();
        float originalSaturation = originalNutrition * food.saturation();

        int desiredNutrition = Math.round(originalNutrition * nutritionMultiplier);

        float desiredSaturation = originalSaturation * saturationMultiplier;

        ItemStack result = super.finishUsing(stack, world, user);

        player.getHungerManager().add(desiredNutrition - originalNutrition, desiredSaturation - originalSaturation);

        manager.addCorruption(player.getUuid(), corruptionAmount);

        Butcher.LOGGER.info(
                "{} corruption: {} (stage {})",
                player.getName().getString(),
                manager.getCorruption(player.getUuid()),
                manager.getStage(player.getUuid())
        );

        return result;
    }
}