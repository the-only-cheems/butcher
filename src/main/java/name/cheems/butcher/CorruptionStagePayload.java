package name.cheems.butcher;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record CorruptionStagePayload(int stage) implements CustomPayload {

    public static final Id<CorruptionStagePayload> ID =
            new Id<>(Identifier.of(Butcher.MOD_ID, "corruption_stage"));

    public static final PacketCodec<RegistryByteBuf, CorruptionStagePayload> CODEC =
            PacketCodec.tuple(PacketCodecs.VAR_INT, CorruptionStagePayload::stage, CorruptionStagePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}