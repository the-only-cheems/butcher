package name.cheems.butcher.teleport;

import name.cheems.butcher.Butcher;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MeatPortalTeleport {

    private static final int ANIMATION_TICKS = 24;

    private static final double ANIMATION_DISTANCE = 1.2;

    private static final List<TeleportAnimation> ACTIVE_ANIMATIONS =
            new ArrayList<>();

    public static void register() {

        Butcher.LOGGER.info("Registering teleporting for " + Butcher.MOD_ID);

        ServerTickEvents.END_SERVER_TICK.register(server -> {

            Iterator<TeleportAnimation> iterator =
                    ACTIVE_ANIMATIONS.iterator();

            while (iterator.hasNext()) {

                TeleportAnimation animation = iterator.next();

                if (animation.tick()) {
                    iterator.remove();
                }
            }
        });
    }

    public static void start(
            ServerPlayerEntity player,
            BlockPos entrance,
            BlockPos destination
    )
    {
        // Prevent multiple teleport animations at once.
        for (TeleportAnimation animation : ACTIVE_ANIMATIONS) {
            if (animation.player == player) {
                return;
            }
        }

        ACTIVE_ANIMATIONS.add(
                new TeleportAnimation(
                        player,
                        entrance,
                        destination
                )
        );
    }

    private static class TeleportAnimation {

        private final ServerPlayerEntity player;

        private final BlockPos entrance;
        private final BlockPos destination;

        private int tick = 0;

        private final double entranceX;
        private final double entranceY;
        private final double entranceZ;

        private final double destinationX;
        private final double destinationY;
        private final double destinationZ;

        private boolean hasTeleported = false;

        private TeleportAnimation(
                ServerPlayerEntity player,
                BlockPos entrance,
                BlockPos destination
        )
        {
            this.player = player;
            this.entrance = entrance;
            this.destination = destination;

            this.entranceX = entrance.getX() + 0.5;
            this.entranceY = entrance.getY() + 1.0;
            this.entranceZ = entrance.getZ() + 0.5;

            this.destinationX = destination.getX() + 0.5;
            this.destinationY = destination.getY() + 1.2;
            this.destinationZ = destination.getZ() + 0.5;

            lockPlayer();
        }

        private boolean tick() {

            if (!player.isAlive()) {
                return true;
            }

            lockPlayer();

            //going in
            if (!hasTeleported) {


                double progress =
                        (double) tick / ANIMATION_TICKS;

                // Ease-in effect.
                double eased =
                        progress * progress;

                double y =
                        entranceY - (eased * ANIMATION_DISTANCE);

                player.requestTeleport(
                        entranceX,
                        y,
                        entranceZ
                );

                spawnEntranceParticles();

                tick++;

                if (tick >= ANIMATION_TICKS) {

                    player.teleport(
                            (ServerWorld) player.getWorld(),
                            destinationX,
                            destinationY - ANIMATION_DISTANCE,
                            destinationZ,
                            player.getYaw(),
                            player.getPitch()
                    );

                    hasTeleported = true;
                    tick = 0;
                }

                return false;
            }

            //coming out

            double progress =
                    (double) tick / ANIMATION_TICKS;

            // Ease-out effect.
            double eased =
                    1.0 - ((1.0 - progress) * (1.0 - progress));

            double y =
                    destinationY - ANIMATION_DISTANCE + (eased * ANIMATION_DISTANCE);

            player.requestTeleport(
                    destinationX,
                    y,
                    destinationZ
            );

            spawnExitParticles();

            tick++;

            unlockPlayer();

            return tick >= ANIMATION_TICKS;
        }

        private void lockPlayer(){
            player.setVelocity(0,0,0);

            player.fallDistance = 0;

            player.setNoGravity(true);
        }

        private void unlockPlayer(){
            player.setVelocity(0,0,0);

            player.fallDistance = 0;

            player.setNoGravity(false);
        }

        private void spawnEntranceParticles() {

            ServerWorld wololo = (ServerWorld) player.getWorld();

            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();

            wololo.spawnParticles(
                    ParticleTypes.PORTAL,
                    x,
                    y,
                    z,
                    8,
                    0.35,
                    0.1,
                    0.35,
                    0.05
            );
            wololo.spawnParticles(
                    ParticleTypes.CRIMSON_SPORE,
                    x,
                    y,
                    z,
                    2,
                    0.25,
                    0.1,
                    0.25,
                    0.01
            );
        }

        private void spawnExitParticles() {
            ServerWorld wololo = (ServerWorld) player.getWorld();

            double x = player.getX();
            double y = player.getY() + 0.1;
            double z = player.getZ();

            wololo.spawnParticles(
                    ParticleTypes.PORTAL,
                    x,
                    y,
                    z,
                    8,
                    0.35,
                    0.1,
                    0.35,
                    0.05
            );
            wololo.spawnParticles(
                    ParticleTypes.CRIMSON_SPORE,
                    x,
                    y,
                    z,
                    2,
                    0.25,
                    0.1,
                    0.25,
                    0.01
            );
        }

        private void playTeleportSound() {
            ServerWorld wololo = (ServerWorld) player.getWorld();
            wololo.playSound(null,player.getBlockPos(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.BLOCKS, 1f, 0.8f);
        }
    }
}
