package name.cheems.butcher.mixin;

import name.cheems.butcher.fluid.ModFluids;
import name.cheems.butcher.particle.ModParticles;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "onSwimmingStart", at = @At("HEAD"), cancellable = true)
    private void butcher$onSwimmingStart(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        FluidState fluidState = player.getWorld().getFluidState(BlockPos.ofFloored(player.getX(), player.getY(), player.getZ()));

        if (fluidState.isOf(ModFluids.STILL_BLOOD) || fluidState.isOf(ModFluids.FLOWING_BLOOD)) {

            player.getWorld().addParticle(ModParticles.BLOOD_PARTICLE, player.getX(), player.getY()+1, player.getZ(), 0.0f, 0.0f, 0.0f);

            ci.cancel();
        }
    }
}