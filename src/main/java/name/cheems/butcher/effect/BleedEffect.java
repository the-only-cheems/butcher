package name.cheems.butcher.effect;

import name.cheems.butcher.damage.ModDamageTypes;
import name.cheems.butcher.particle.ModParticles;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BleedEffect extends StatusEffect {
    public BleedEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        DamageSource source = new DamageSource(
                entity.getWorld()
                        .getRegistryManager()
                        .getWrapperOrThrow(RegistryKeys.DAMAGE_TYPE)
                        .getOrThrow(ModDamageTypes.BLEED)
        );



        //This should (DOES) get rid of KB
        Vec3d oldVelocity = entity.getVelocity();

        entity.damage(source, 1.0F);

        entity.setVelocity(oldVelocity);

        if (entity.getWorld() instanceof ServerWorld serverWorld){
            serverWorld.spawnParticles(
                    ModParticles.BLOOD_PARTICLE,
                    entity.getX(),
                    entity.getY() + 1,
                    entity.getZ(),
                    10,
                    0.2,0.5,0.2,
                    0.0
            );
        }


        //Just in case everything fucks up
        //entity.damage(entity.getDamageSources().magic(), 1.0F);

        return super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 30 >> amplifier;
        return i > 0 ? duration % i == 0 : true;
    }
}
