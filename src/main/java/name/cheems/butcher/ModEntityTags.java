package name.cheems.butcher;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModEntityTags {

    public static void registerEntityTags() {
        Butcher.LOGGER.info("Registering entity tags for " + Butcher.MOD_ID);
    }

    public static final TagKey<EntityType<?>> CAN_BE_BUTCHERED =
            TagKey.of(
                    RegistryKeys.ENTITY_TYPE,
                    Identifier.of(Butcher.MOD_ID, "can_be_butchered")
            );
}