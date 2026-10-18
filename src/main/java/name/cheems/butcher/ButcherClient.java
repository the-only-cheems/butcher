package name.cheems.butcher;

import name.cheems.butcher.block.entity.ModBlockEntities;
import name.cheems.butcher.block.entity.renderer.ToolRackEntityRenderer;
import name.cheems.butcher.client.CorruptionClientData;
import name.cheems.butcher.client.CorruptionHud;
import name.cheems.butcher.fluid.ModFluids;
import name.cheems.butcher.particle.BloodParticle;
import name.cheems.butcher.particle.ModParticles;
import name.cheems.butcher.screen.ModScreenHandlers;
import name.cheems.butcher.screen.custom.MeatGrinderScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.Identifier;


public class ButcherClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ParticleFactoryRegistry.getInstance().register(ModParticles.BLOOD_PARTICLE, BloodParticle.Factory::new);

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_BLOOD, ModFluids.FLOWING_BLOOD,
                new SimpleFluidRenderHandler(
                        Identifier.of("minecraft:block/water_still"),
                        Identifier.of("minecraft:block/water_flow"),
                        0xA1FF0000)
                );

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(),
                ModFluids.STILL_BLOOD, ModFluids.FLOWING_BLOOD);

        BlockEntityRendererFactories.register(ModBlockEntities.TOOL_RACK_BE, ToolRackEntityRenderer::new);

        HandledScreens.register(ModScreenHandlers.MEAT_GRINDER_SCREEN_HANDLER, MeatGrinderScreen::new);

        //More Corruption Stuff
        ClientPlayNetworking.registerGlobalReceiver(CorruptionStagePayload.ID,
                (payload, context) -> context.client().execute(() -> CorruptionClientData.setStage(payload.stage())));

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> CorruptionClientData.reset());

        CorruptionHud.register();

    }
}
