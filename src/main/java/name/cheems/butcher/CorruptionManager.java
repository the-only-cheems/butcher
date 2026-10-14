package name.cheems.butcher;

import name.cheems.butcher.effect.ModEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class CorruptionManager extends PersistentState {
    public static final int MAX_CORRUPTION = 100;
    public static final int DECAY_AMOUNT = 15;
    public static final long TICKS_PER_DAY = 24_000L;

    private static final String DATA_KEY = Butcher.MOD_ID + "_corruption";

    private static final Type<CorruptionManager> TYPE = new Type<>(CorruptionManager::new,
            (nbt, registryLookup) -> fromNbt(nbt), null);

    private final Map<UUID, Integer> scores = new HashMap<>();

    private final Map<UUID, Long> lastDecayTick = new HashMap<>();

    private CorruptionManager() {
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtList list = new NbtList();

        for (Map.Entry<UUID, Integer> entry : scores.entrySet()) {
            NbtCompound playerData = new NbtCompound();
            playerData.putUuid("UUID", entry.getKey());
            playerData.putInt("Score", entry.getValue());
            list.add(playerData);
        }

        nbt.put("Players", list);
        return nbt;
    }

    public static CorruptionManager get(MinecraftServer server) {
        ServerWorld overworld = server.getWorld(World.OVERWORLD);

        if (overworld == null) {
            throw new IllegalStateException("Overworld is unavailable");
        }

        PersistentStateManager stateManager =
                overworld.getPersistentStateManager();

        return stateManager.getOrCreate(TYPE, DATA_KEY);
    }

    private void updateItchingEffect(ServerPlayerEntity player) {
        int stage = getStage(player.getUuid());

        if (stage == 0 || stage == 3) {
            player.removeStatusEffect(ModEffects.ITCHING);
            return;
        }

        int amplifier = stage - 1;

        StatusEffectInstance currentEffect = player.getStatusEffect(ModEffects.ITCHING);

        if (currentEffect == null || currentEffect.getAmplifier() != amplifier || currentEffect.getDuration() <= 10) {

            player.addStatusEffect(new StatusEffectInstance(ModEffects.ITCHING, 200, amplifier, false, false, true));
        }
    }

    public int getCorruption(UUID playerId) {
        return scores.getOrDefault(playerId, 0);
    }

    public int getStage(UUID playerId) {
        return getStageForScore(getCorruption(playerId));
    }

    public static int getStageForScore(int score) {
        if (score >= 75) return 3;
        if (score >= 50) return 2;
        if (score >= 25) return 1;
        return 0;
    }

    public int addCorruption(UUID playerId, int amount) {
        if (amount <= 0) {
            return getCorruption(playerId);
        }

        int oldScore = getCorruption(playerId);
        int newScore = (int) Math.min(
                MAX_CORRUPTION,
                (long) oldScore + amount
        );

        if (newScore != oldScore) {
            scores.put(playerId, newScore);
            markDirty();
        }

        return newScore;
    }

    public int removeCorruption(UUID playerId, int amount) {
        if (amount <= 0) {
            return getCorruption(playerId);
        }

        int oldScore = getCorruption(playerId);
        int newScore = Math.max(0, oldScore - amount);

        if (newScore != oldScore) {
            if (newScore == 0) {
                scores.remove(playerId);
            } else {
                scores.put(playerId, newScore);
            }

            markDirty();
        }

        return newScore;
    }

    public void tickPlayer(ServerPlayerEntity player) {
        updateItchingEffect(player);

        UUID playerId = player.getUuid();
        long now = player.getServerWorld().getTime();

        Long previous = lastDecayTick.putIfAbsent(playerId, now);

        if (previous == null) {
            return;
        }

        long elapsed = now - previous;

        if (elapsed < 0) {
            lastDecayTick.put(playerId, now);
            return;
        }

        long daysElapsed = elapsed / TICKS_PER_DAY;

        if (daysElapsed <= 0) {
            return;
        }

        long elapsedTicks = daysElapsed * TICKS_PER_DAY;
        lastDecayTick.put(playerId, previous + elapsedTicks);

        int score = getCorruption(playerId);

        if (score <= 0) {
            return;
        }

        long totalDecay = daysElapsed * DECAY_AMOUNT;
        int newScore = (int) Math.max(0L, score - totalDecay);

        if (newScore != score) {
            if (newScore == 0) {
                scores.remove(playerId);
            } else {
                scores.put(playerId, newScore);
            }

            markDirty();
        }
    }

    public void onPlayerLeave(UUID playerId) {
        lastDecayTick.remove(playerId);
    }

    private static CorruptionManager fromNbt(NbtCompound nbt) {
        CorruptionManager manager = new CorruptionManager();
        NbtList list = nbt.getList("Players", NbtElement.COMPOUND_TYPE);

        for (int i = 0; i < list.size(); i++) {
            NbtCompound entry = list.getCompound(i);

            try {
                UUID uuid = entry.getUuid("UUID");
                int score = entry.getInt("Score");
                score = Math.max(0, Math.min(MAX_CORRUPTION, score));

                if (score > 0) {
                    manager.scores.put(uuid, score);
                }
            } catch (IllegalArgumentException exception) {
                Butcher.LOGGER.warn("Skipping invalid corruption entry in saved data", exception);
            }
        }

        return manager;
    }
}