package io.github.nwrenger.disabledimensionsreimagined.platform.services;

import java.nio.file.Path;

public interface IPlatformHelper {
    /**
     * Gets the path to the configuration directory.
     *
     * @return The path to the configuration directory.
     */
    Path getConfigDir();

    /**
     * Registers commands for the mod.
     */
    void registerCommands();
}
