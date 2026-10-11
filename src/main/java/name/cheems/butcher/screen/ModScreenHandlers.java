package name.cheems.butcher.screen;

import name.cheems.butcher.Butcher;
import name.cheems.butcher.screen.custom.MeatGrinderScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {
    public static final ScreenHandlerType<MeatGrinderScreenHandler> MEAT_GRINDER_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Butcher.MOD_ID, "meat_grinder_screen_handler"),
                    new ExtendedScreenHandlerType<>(MeatGrinderScreenHandler::new, BlockPos.PACKET_CODEC));

    public static void registerScreenHandlers() {
        Butcher.LOGGER.info("Registering Screen Handlers for " + Butcher.MOD_ID);
    }
}
