package io.github.nwrenger.disabledimensionsreimagined.mixin;

import io.github.nwrenger.disabledimensionsreimagined.DimensionTravel;
import net.minecraft.advancements.criterion.ChangeDimensionTrigger;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChangeDimensionTrigger.class)
public class ChangeDimensionTriggerMixin {

    @Inject(method = "trigger", at = @At("HEAD"))
    private void disabledimensionsreimagined$detectDimensionChange(
        ServerPlayer player,
        ResourceKey<Level> from,
        ResourceKey<Level> to,
        CallbackInfo callback
    ) {
        DimensionTravel recovery = (DimensionTravel) player;
        recovery.disabledimensionsreimagined$checkTeleport(from, to);
    }
}
