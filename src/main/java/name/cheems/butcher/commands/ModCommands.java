package name.cheems.butcher.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import name.cheems.butcher.Butcher;
import name.cheems.butcher.CorruptionManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public final class ModCommands {

    private ModCommands() {
    }

    public static void register() {
        Butcher.LOGGER.info("Registering commands for "+ Butcher.MOD_ID);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> registerCommands(dispatcher));
    }

    private static void registerCommands(
            CommandDispatcher<ServerCommandSource> dispatcher
    ) {
        dispatcher.register(
                CommandManager.literal("addcorruption")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                        .then(CommandManager.argument("amount", IntegerArgumentType.integer(1)).executes(context -> {
                                                            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");

                                                            int amount = IntegerArgumentType.getInteger(context, "amount");

                                                            CorruptionManager manager = CorruptionManager.get(context.getSource().getServer());

                                                            int newScore = manager.addCorruption(player.getUuid(), amount);

                                                            context.getSource().sendFeedback(() -> Text.literal("Added " + amount + " corruption to "
                                                                    + player.getName().getString() + ". New total: " + newScore + "/"
                                                                    + CorruptionManager.MAX_CORRUPTION), true);

                                                            return newScore;
                                                        }))));

        dispatcher.register(CommandManager.literal("removecorruption").requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                        .then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
                                                        .executes(context -> {
                                                            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");

                                                            int amount = IntegerArgumentType.getInteger(context, "amount");

                                                            CorruptionManager manager = CorruptionManager.get(context.getSource().getServer());

                                                            int newScore = manager.removeCorruption(player.getUuid(), amount);

                                                            context.getSource().sendFeedback(() -> Text.literal("Removed " + amount + " corruption from "
                                                                                    + player.getName().getString() + ". New total: " + newScore + "/"
                                                                                    + CorruptionManager.MAX_CORRUPTION), true);
                                                            return newScore;
                                                        }))));
    }
}