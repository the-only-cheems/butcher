package name.cheems.butcher;

import name.cheems.butcher.block.ModBlocks;
import name.cheems.butcher.item.ModItemGroups;
import name.cheems.butcher.item.ModItems;
import name.cheems.butcher.teleport.MeatPortalTeleport;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Butcher implements ModInitializer {
	public static final String MOD_ID = "butcher";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		MeatPortalTeleport.register();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModEntityTags.registerEntityTags();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
