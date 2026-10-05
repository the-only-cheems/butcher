package name.cheems.butcher.teleport;

import name.cheems.butcher.Butcher;
import name.cheems.butcher.particle.ModParticles;
import name.cheems.butcher.sound.ModSounds;
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

    private static boolean HEARING = false;

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

        private final boolean originalNoClip;

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

            this.originalNoClip = player.noClip;

            lockPlayer();
        }

        private boolean tick() {

            if (!player.isAlive()) {
                return true;
            }

            lockPlayer();

            if (!hasTeleported) {

                double progress = (double) tick / ANIMATION_TICKS;
                double eased = progress * progress;

                double y = entranceY - (eased * ANIMATION_DISTANCE);

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

            double progress = (double) tick / ANIMATION_TICKS;

            double eased =
                    1.0 - ((1.0 - progress) * (1.0 - progress));

            double y =
                    destinationY - ANIMATION_DISTANCE
                            + (eased * ANIMATION_DISTANCE);

            player.requestTeleport(
                    destinationX,
                    y,
                    destinationZ
            );

            spawnExitParticles();

            tick++;

            if (tick >= ANIMATION_TICKS) {
                unlockPlayer();
                return true;
            }

            return false;
        }

        private void lockPlayer(){
            ServerWorld wololo = (ServerWorld) player.getWorld();


            if (!HEARING){
                wololo.playSound(null, player.getBlockPos(), ModSounds.MEAT_PORTAL_TELEPORT, SoundCategory.BLOCKS);
                HEARING = true;
            }

            player.noClip = true;

            player.setVelocity(0,0,0);

            player.fallDistance = 0;

            player.setNoGravity(true);
        }

        private void unlockPlayer(){
            player.noClip = originalNoClip;

            player.setVelocity(0,0,0);

            player.fallDistance = 0;

            player.setNoGravity(false);

            HEARING=false;
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
                    0.25,
                    0.35,
                    0.05
            );
            wololo.spawnParticles(
                    ModParticles.BLOOD_PARTICLE,
                    x,
                    y,
                    z,
                    10,
                    0.2,
                    0.1,
                    0.2,
                    0
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
                    ModParticles.BLOOD_PARTICLE,
                    x,
                    y,
                    z,
                    10,
                    0.2,
                    0.1,
                    0.2,
                    0
            );
        }

        private void playTeleportSound() {
            ServerWorld wololo = (ServerWorld) player.getWorld();
            wololo.playSound(null,player.getBlockPos(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.BLOCKS, 1f, 0.8f);
        }
    }
}
