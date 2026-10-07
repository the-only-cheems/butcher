package name.cheems.butcher.mixin;

import name.cheems.butcher.fluid.ModFluids;
import name.cheems.butcher.particle.ModParticles;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "onSwimmingStart", at = @At("HEAD"), cancellable = true)
    private void butcher$bloodSwimmingParticles(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;

        Box box = entity.getBoundingBox();

        BlockPos minPos = BlockPos.ofFloored(box.minX, box.minY, box.minZ);
        BlockPos maxPos = BlockPos.ofFloored(box.maxX, box.maxY, box.maxZ);

        for (int x = minPos.getX(); x <= maxPos.getX(); x++) {
            for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
                for (int z = minPos.getZ(); z <= maxPos.getZ(); z++) {

                    FluidState fluidState = entity.getWorld()
                            .getFluidState(new BlockPos(x, y, z));

                    if (fluidState.isOf(ModFluids.STILL_BLOOD)
                            || fluidState.isOf(ModFluids.FLOWING_BLOOD)) {

                        entity.getWorld().addParticle(
                                ModParticles.BLOOD_PARTICLE,
                                entity.getX(),
                                entity.getY() + 1.0,
                                entity.getZ(),
                                0.0,
                                0.0,
                                0.0
                        );

                        ci.cancel();
                        return;
                    }
                }
            }
        }
    }
}