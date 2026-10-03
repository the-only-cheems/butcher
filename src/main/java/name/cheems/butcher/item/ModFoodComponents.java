package name.cheems.butcher.item;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class ModFoodComponents {

    public static final FoodComponent MYSTERY_MEAT = new FoodComponent.Builder().nutrition(3).saturationModifier(0.25f)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200), 0.25f).build();

    public static final FoodComponent MYSTERY_MEAT_COOKED = new FoodComponent.Builder().nutrition(6).saturationModifier(1.2f).build();
}
