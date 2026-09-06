package io.github.nwrenger.disabledimensionsreimagined;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class DisableDimensionsReimagined {

    public DisableDimensionsReimagined(IEventBus eventBus) {
        Common.init();
    }
}
