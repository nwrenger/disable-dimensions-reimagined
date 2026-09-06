package io.github.nwrenger.disabledimensionsreimagined;

import io.github.nwrenger.disabledimensionsreimagined.config.Config;
import io.github.nwrenger.disabledimensionsreimagined.config.Dimension;
import io.github.nwrenger.disabledimensionsreimagined.platform.Services;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class Common {

    private static Config config;

    public static void init() {
        loadConfig();

        // Command registration for the `/ddr` and `/disabledimensionsreimagined` commands
        Services.PLATFORM.registerCommands();

        Constants.LOG.info(
            "[Disable Dimensions Reimagined] Started successfully"
        );
    }

    public static boolean isDimensionDisabled(
        ResourceLocation id,
        Entity entity
    ) {
        Dimension dimension = config.getDimension(id);
        if (dimension == null) {
            return false;
        }

        return dimension.isDisabled(entity);
    }

    public static Config getConfig() {
        return Objects.requireNonNull(config);
    }

    public static void loadConfig() {
        // Load and update state
        Config newConfig = Config.load();

        // Validate the config to ensure that all values are within acceptable ranges
        Config.validate(newConfig);

        // All is validated, so apply
        config = newConfig;
    }
}
