package io.github.nwrenger.disabledimensionsreimagined.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.nwrenger.disabledimensionsreimagined.platform.Services;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public class Config {

    private static final File CONFIG_FILE = new File(
        Services.PLATFORM.getConfigDir().toFile(),
        "disable-dimensions-reimagined.json"
    );

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .create();

    public Map<String, Dimension> dimensions = defaults();

    @Nullable
    public Dimension getDimension(Identifier key) {
        return dimensions.get(key.toString());
    }

    private static Map<String, Dimension> defaults() {
        Map<String, Dimension> dimensions = new LinkedHashMap<>();

        dimensions.put(
            "minecraft:the_nether",
            new Dimension(
                true,
                new Message(
                    "A sinister presence denies your passage...",
                    "dark_red"
                ),
                new Condition[0]
            )
        );

        dimensions.put(
            "minecraft:the_end",
            new Dimension(
                true,
                new Message("The End rejects your presence...", "light_purple"),
                new Condition[0]
            )
        );

        return dimensions;
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_FILE.toPath())) {
            GSON.toJson(this, writer);
        } catch (IOException exception) {
            throw new UncheckedIOException(
                "[Disable Dimensions Reimagined] Unable to save config",
                exception
            );
        }
    }

    public static @Nullable Config load() {
        if (!CONFIG_FILE.exists()) {
            Config newConfig = new Config();
            newConfig.save();
            return newConfig;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_FILE.toPath())) {
            return GSON.fromJson(reader, Config.class);
        } catch (IOException exception) {
            throw new UncheckedIOException(
                "[Disable Dimensions Reimagined] Unable to read config",
                exception
            );
        }
    }

    public static void validate(@Nullable Config config) {
        if (config == null) {
            throw new IllegalStateException(
                "[Disable Dimensions Reimagined] Config must not be null"
            );
        }

        if (config.dimensions == null) {
            throw new IllegalStateException(
                "[Disable Dimensions Reimagined] `dimensions` must not be null"
            );
        }

        for (Map.Entry<
            String,
            Dimension
        > entry : config.dimensions.entrySet()) {
            String dimensionId = entry.getKey();
            if (
                dimensionId == null ||
                !dimensionId.contains(":") ||
                Identifier.tryParse(dimensionId) == null
            ) {
                throw new IllegalStateException(
                    "[Disable Dimensions Reimagined] Invalid dimension ID: " +
                        dimensionId
                );
            }

            Dimension.validate(entry.getValue());
        }
    }
}
