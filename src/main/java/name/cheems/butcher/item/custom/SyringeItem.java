package name.cheems.butcher.item.custom;

import name.cheems.butcher.Butcher;
import name.cheems.butcher.effect.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SyringeItem extends Item {
    public SyringeItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int roll = (int)(Math.random() * 6);

        if (roll == 0) {
            target.addStatusEffect(new StatusEffectInstance(ModEffects.BLEED,
                    120,
                    0,
                    false,
                    false,
                    true));
            Butcher.LOGGER.info("Bleed");
        }
        else if (roll == 1) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,
                    120,
                    0,
                    false,
                    true,
                    true));
            Butcher.LOGGER.info("Infested");
        }
        else if (roll == 2) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON,
                    120,
                    0,
                    false,
                    true,
                    true));
            Butcher.LOGGER.info("Poison");
        }
        else if (roll == 3) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE,
                    120,
                    0,
                    false,
                    false,
                    true));
            Butcher.LOGGER.info("Itching");
        }
        else if (roll == 4) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER,
                    120,
                    0,
                    false,
                    true,
                    true));
            Butcher.LOGGER.info("Hunger");
        }
        else if (roll == 5) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,
                    120,
                    0,
                    false,
                    true,
                    true));
            Butcher.LOGGER.info("Slowness");
        }

    }
}
