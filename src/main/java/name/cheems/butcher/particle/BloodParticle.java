package name.cheems.butcher.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class BloodParticle extends SpriteBillboardParticle{

    private boolean landed = false;

    public BloodParticle(ClientWorld clientWorld, double x, double y, double z,
                         SpriteProvider spriteProvider, double xSpeed, double ySpeed, double zSpeed) {
        super(clientWorld, x, y, z);
        this.gravityStrength = 0.8F;
        this.scale=(float)((Math.random() * (0.1 - 0.075)) + 0.075);
        this.collidesWithWorld = true;

        this.maxAge = 400;

        float brightness = (float)((Math.random() * (1.0 - 0.5)) + 0.5);

        this.setColor(brightness, brightness, brightness);
        this.setAlpha(1.0f);

        if (ySpeed == 0.0 && (xSpeed != 0.0 || zSpeed != 0.0)) {
            this.velocityX = xSpeed;
            this.velocityY = 0.1;
            this.velocityZ = zSpeed;
        }
    }

    @Override
    public void tick() {
        if (this.landed) {

            this.age++;

            float alpha = 1.0f - ((float) this.age / maxAge);

            this.setAlpha(alpha);

            if (this.age >= this.maxAge) {
                this.markDead();
            }

            return;
        }

        super.tick();

        if (this.onGround && !landed) {
            this.landed = true;

            this.age = 0;

            this.velocityX = 0.0;
            this.velocityY = 0.0;
            this.velocityZ = 0.0;

            this.gravityStrength = 0.0F;
            this.collidesWithWorld = false;

            this.setPos(this.x, this.y + 0.01, this.z);

            // IMPORTANT: kill interpolation
            this.prevPosX = this.x;
            this.prevPosY = this.y;
            this.prevPosZ = this.z;
        }
    }


    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
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
