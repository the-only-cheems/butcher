package name.cheems.butcher.particle;

import name.cheems.butcher.Butcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class BloodParticle extends RainSplashParticle{
    public BloodParticle(ClientWorld clientWorld, double x, double y, double z,
                         SpriteProvider spriteProvider, double xSpeed, double ySpeed, double zSpeed) {
        super(clientWorld, x, y, z);
        this.gravityStrength = 0.04F;
        this.scale=0.1f;
        this.collidesWithWorld = true;

        this.maxAge = 40;

        float brightness = (float)((Math.random() * (1.0 - 0.5)) + 0.5);

        Butcher.LOGGER.info(String.valueOf(brightness));

        this.setColor(brightness, brightness, brightness);

        if (ySpeed == 0.0 && (xSpeed != 0.0 || zSpeed != 0.0)) {
            this.velocityX = xSpeed;
            this.velocityY = 0.1;
            this.velocityZ = zSpeed;
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            BloodParticle bloodParticle = new BloodParticle(world, x, y, z, this.spriteProvider, velocityX, velocityY, velocityZ);
            bloodParticle.setSprite(this.spriteProvider.getSprite(world.random));
            return bloodParticle;
        }
    }
}
