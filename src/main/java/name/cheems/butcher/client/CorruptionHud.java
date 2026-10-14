package name.cheems.butcher.client;

import name.cheems.butcher.Butcher;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public final class CorruptionHud {

    private static final Identifier STAGE_1 = Identifier.of(Butcher.MOD_ID, "textures/gui/corruption_stage_1.png");

    private static final Identifier STAGE_2 = Identifier.of(Butcher.MOD_ID, "textures/gui/corruption_stage_2.png");

    private static final Identifier STAGE_3 = Identifier.of(Butcher.MOD_ID, "textures/gui/corruption_stage_3.png");

    private static final int ICON_SIZE = 24;
    private static final int MARGIN = 8;

    private CorruptionHud() {
    }

    public static void register() {
        HudRenderCallback.EVENT.register(CorruptionHud::render);
    }

    private static void render(DrawContext context, net.minecraft.client.render.RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.world == null) {
            return;
        }

        Identifier texture = switch (CorruptionClientData.getStage()) {
            case 1 -> STAGE_1;
            case 2 -> STAGE_2;
            case 3 -> STAGE_3;
            default -> null;
        };

        if (texture == null) {
            return;
        }

        int y = context.getScaledWindowHeight() - ICON_SIZE - MARGIN;

        context.drawTexture(texture, MARGIN, y, 0.0F, 0.0F, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
    }
}