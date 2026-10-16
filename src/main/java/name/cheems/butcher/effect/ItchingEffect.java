package name.cheems.butcher.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class ItchingEffect extends StatusEffect {
    public ItchingEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity player && !player.getWorld().isClient()) {

            // Stupid maths I don't really understand but it works
            double angle = player.getRandom().nextDouble() * Math.PI * 2;

            Vec3d direction = new Vec3d(Math.cos(angle), 0, Math.sin(angle));

            double strength = 0.4;

            player.setVelocity(direction.x * strength, 0.2, direction.z * strength);

            player.velocityModified = true;
        }

        return super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int interval = Math.max(1, 100 - (amplifier * 5));
        return duration % interval == 0;
    }
}
