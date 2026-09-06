package io.github.nwrenger.disabledimensionsreimagined.platform;

import io.github.nwrenger.disabledimensionsreimagined.Constants;
import io.github.nwrenger.disabledimensionsreimagined.platform.services.IPlatformHelper;
import java.util.ServiceLoader;

// Service loaders are a built-in Java feature that allow us to locate implementations of an interface that vary from one
// environment to another. In the context of MultiLoader we use this feature to access a mock API in the common code that
// is swapped out for the platform specific implementation at runtime.
public class Services {

    // Platform helper service that provides platform-specific functionality. This
    // service is loaded using the ServiceLoader mechanism, which allows us to
    // provide different implementations for different platforms (e.g., Forge,
    // Fabric).
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    // This code is used to load a service for the current environment. The
    // implementation of the service is defined manually by including a text file in
    // META-INF/services named with the fully
    // qualified class name of the service. Inside the file the fully qualified
    // class name of the implementation is specified to load for the platform. It
    // points for Forge to ForgePlatformHelper while for Fabric to
    // FabricPlatformHelper.
    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(
            clazz,
            Services.class.getClassLoader()
        )
            .findFirst()
            .orElseThrow(() ->
                new NullPointerException(
                    "[Disable Dimensions Reimagined] Failed to load service for " +
                        clazz.getName()
                )
            );
        Constants.LOG.debug(
            "[Disable Dimensions Reimagined] Loaded {} for service {}",
            loadedService,
            clazz
        );
        return loadedService;
    }
}
