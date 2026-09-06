package io.github.nwrenger.disabledimensionsreimagined.platform;

import io.github.nwrenger.disabledimensionsreimagined.cmd.Command;
import io.github.nwrenger.disabledimensionsreimagined.platform.services.IPlatformHelper;
import java.nio.file.Path;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public void registerCommands() {
        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) ->
                Command.register(dispatcher)
        );
    }
}
