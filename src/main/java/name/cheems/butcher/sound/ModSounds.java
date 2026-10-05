package name.cheems.butcher.sound;

import name.cheems.butcher.Butcher;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent MEAT_PORTAL_TELEPORT = regiseterSoundEvent("meat_portal_teleport");

    public static final SoundEvent FLESH_BLOCK_BREAK = regiseterSoundEvent("flesh_block_break");
    public static final SoundEvent FLESH_BLOCK_STEP = regiseterSoundEvent("flesh_block_step");
    public static final SoundEvent FLESH_BLOCK_PLACE = regiseterSoundEvent("flesh_block_place");
    public static final SoundEvent FLESH_BLOCK_HIT = regiseterSoundEvent("flesh_block_hit");
    public static final SoundEvent FLESH_BLOCK_FALL = regiseterSoundEvent("flesh_block_fall");


    public static final BlockSoundGroup FLESH_BLOCK_SOUNDS = new BlockSoundGroup(1f,1f,
            FLESH_BLOCK_BREAK, FLESH_BLOCK_STEP, FLESH_BLOCK_PLACE, FLESH_BLOCK_HIT, FLESH_BLOCK_FALL);


    private static SoundEvent regiseterSoundEvent(String name) {
        Identifier id = Identifier.of(Butcher.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        Butcher.LOGGER.info("Registering sounds for "+ Butcher.MOD_ID);
    }
}
