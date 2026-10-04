package name.cheems.butcher.effect;

import name.cheems.butcher.Butcher;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEffects {

    public static final RegistryEntry<StatusEffect> BLEED = registerStatusEffect("bleed",
            new BleedEffect(StatusEffectCategory.HARMFUL, 0xff0000));

    private static RegistryEntry<StatusEffect> registerStatusEffect(String name, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Butcher.MOD_ID, name), statusEffect);
    }

    public static void registerEffects() {
        Butcher.LOGGER.info("Registering effects for " + Butcher.MOD_ID);
    }
}
