package name.cheems.butcher;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class CorruptionSync {

    private static final Map<UUID, Integer> LAST_SENT_STAGE = new HashMap<>();

    private CorruptionSync() {
    }

    public static void registerCommon() {
        PayloadTypeRegistry.playS2C().register(CorruptionStagePayload.ID, CorruptionStagePayload.CODEC);

        // Send the current stage when a player joins.
        ServerPlayConnectionEvents.JOIN.register(
                (handler, sender, server) -> {
                    ServerPlayerEntity player = handler.player;

                    int stage = CorruptionManager.get(server).getStage(player.getUuid());

                    LAST_SENT_STAGE.put(player.getUuid(), stage);

                    ServerPlayNetworking.send(player, new CorruptionStagePayload(stage));
                });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

                UUID playerId = player.getUuid();

                int stage = CorruptionManager.get(server).getStage(playerId);

                Integer previous = LAST_SENT_STAGE.put(playerId, stage);

                if (previous == null || previous != stage) {
                    ServerPlayNetworking.send(player, new CorruptionStagePayload(stage));
                }
            }
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> LAST_SENT_STAGE.remove(handler.player.getUuid()));
    }
}