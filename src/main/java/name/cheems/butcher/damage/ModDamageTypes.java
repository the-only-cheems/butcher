package name.cheems.butcher.damage;

import name.cheems.butcher.Butcher;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryKey;

public class ModDamageTypes {

    public static void registerdamagetypes() {
        Butcher.LOGGER.info("Registering damage types for " + Butcher.MOD_ID);
    }

    public static final RegistryKey<DamageType> BLEED =
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Butcher.id("bleed"));
}
