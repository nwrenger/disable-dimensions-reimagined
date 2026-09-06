package io.github.nwrenger.disabledimensionsreimagined.mixin;

import io.github.nwrenger.disabledimensionsreimagined.Common;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "changeDimension", at = @At("HEAD"), cancellable = true)
    private void disabledimensionsreimagined$disableDimensionTravel(
        final DimensionTransition transition,
        CallbackInfoReturnable<Entity> callback
    ) {
        Entity entity = (Entity) (Object) this;
        Level currentLevel = entity.level();
        if (!(currentLevel instanceof ServerLevel serverLevel)) {
            return;
        }

        ServerLevel newLevel = transition.newLevel();
        if (newLevel.dimension() == serverLevel.dimension()) {
            return;
        }

        if (
            !Common.isDimensionDisabled(
                newLevel.dimension().location(),
                entity
            )
        ) {
            return;
        }

        callback.setReturnValue(null);
    }
}
