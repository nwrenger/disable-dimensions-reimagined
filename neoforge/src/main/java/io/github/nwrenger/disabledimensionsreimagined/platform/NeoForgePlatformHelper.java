package io.github.nwrenger.disabledimensionsreimagined.platform;

import io.github.nwrenger.disabledimensionsreimagined.cmd.Command;
import io.github.nwrenger.disabledimensionsreimagined.platform.services.IPlatformHelper;
import java.nio.file.Path;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public void registerCommands() {
        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) ->
            Command.register(event.getDispatcher())
        );
    }
}
