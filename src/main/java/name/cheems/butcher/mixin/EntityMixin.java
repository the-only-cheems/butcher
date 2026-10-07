package name.cheems.butcher.mixin;

import name.cheems.butcher.fluid.ModFluids;
import name.cheems.butcher.particle.ModParticles;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "onSwimmingStart", at = @At("HEAD"), cancellable = true)
    private void butcher$bloodSwimmingParticles(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;

        FluidState fluidState = entity.getWorld().getFluidState(BlockPos.ofFloored(entity.getX(), entity.getY(), entity.getZ()));

        if (fluidState.isOf(ModFluids.STILL_BLOOD) || fluidState.isOf(ModFluids.FLOWING_BLOOD)) {

            entity.getWorld().addParticle(ModParticles.BLOOD_PARTICLE, entity.getX(), entity.getY()+1, entity.getZ(), 0.0, 0.0, 0.0);

            ci.cancel();
        }
    }
}