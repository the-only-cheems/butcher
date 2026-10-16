package name.cheems.butcher;

import name.cheems.butcher.block.ModBlocks;
import name.cheems.butcher.block.ModCauldronBehaviours;
import name.cheems.butcher.block.entity.ModBlockEntities;
import name.cheems.butcher.commands.ModCommands;
import name.cheems.butcher.damage.ModDamageTypes;
import name.cheems.butcher.effect.ModEffects;
import name.cheems.butcher.fluid.ModFluids;
import name.cheems.butcher.item.ModItemGroups;
import name.cheems.butcher.item.ModItems;
import name.cheems.butcher.particle.ModParticles;
import name.cheems.butcher.recipe.ModRecipies;
import name.cheems.butcher.screen.ModScreenHandlers;
import name.cheems.butcher.sound.ModSounds;
import name.cheems.butcher.teleport.MeatPortalTeleport;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
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

		ModFluids.registerModFluids();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModEntityTags.registerEntityTags();
		ModDamageTypes.registerdamagetypes();
		ModEffects.registerEffects();
		ModParticles.registerParticles();
		ModSounds.registerSounds();
		ModCauldronBehaviours.registerModCauldronBehaviours();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();
		ModRecipies.registerRecipes();
		ModCommands.register();

		//Freaky Deaky Corruption Shit
		CorruptionSync.registerCommon();

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			CorruptionManager manager = CorruptionManager.get(server);

			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				manager.tickPlayer(player);
			}
		});

		ServerPlayConnectionEvents.DISCONNECT.register(
				(handler, server) -> {
					CorruptionManager.get(server).onPlayerLeave(handler.player.getUuid());
				}
		);
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
