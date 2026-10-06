package name.cheems.butcher;

import name.cheems.butcher.fluid.ModFluids;
import name.cheems.butcher.particle.BloodParticle;
import name.cheems.butcher.particle.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.render.RenderLayer;
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

    }
}
