package name.cheems.butcher.mixin;

import name.cheems.butcher.Butcher;
import name.cheems.butcher.fluid.ModFluids;
import name.cheems.butcher.particle.ModParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.PointedDripstoneBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PointedDripstoneBlock.class)
public class PointedDripstoneBlockMixin {

    @Inject(
            method = "createParticle(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/Fluid;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void butcher$customDripParticle(World world, BlockPos pos, BlockState state, Fluid fluid, CallbackInfo ci) {
        if (fluid == ModFluids.STILL_BLOOD) {

            Vec3d offset = state.getModelOffset(world, pos);

            double x = pos.getX() + 0.5 + offset.x;
            double y = pos.getY() + 0.25;
            double z = pos.getZ() + 0.5 + offset.z;

            world.addParticle(
                    ModParticles.BLOOD_PARTICLE,
                    x,
                    y,
                    z,
                    0.0,
                    0.0,
                    0.0);

            // Stop vanilla from spawning the water/lava particle
            ci.cancel();
        }
    }
}