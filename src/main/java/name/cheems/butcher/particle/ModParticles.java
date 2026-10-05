package name.cheems.butcher.particle;

import name.cheems.butcher.Butcher;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {

    public static final SimpleParticleType BLOOD_PARTICLE = registerParticle("blood_particle", FabricParticleTypes.simple(true));

    private static SimpleParticleType registerParticle(String name, SimpleParticleType particleType){
        return Registry.register(Registries.PARTICLE_TYPE, Identifier.of(Butcher.MOD_ID, name), particleType);
    }

    public static void registerParticles() {
        Butcher.LOGGER.info("Registering particles for " + Butcher.MOD_ID);
    }
}
