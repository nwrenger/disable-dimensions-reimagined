package io.github.nwrenger.disabledimensionsreimagined;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface DimensionTravel {
    void disabledimensionsreimagined$checkTeleport(
        ResourceKey<Level> from,
        ResourceKey<Level> to
    );
}
