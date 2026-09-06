package io.github.nwrenger.disabledimensionsreimagined.mixin;

import io.github.nwrenger.disabledimensionsreimagined.Common;
import io.github.nwrenger.disabledimensionsreimagined.DimensionTravel;
import io.github.nwrenger.disabledimensionsreimagined.config.Dimension;
import io.github.nwrenger.disabledimensionsreimagined.config.Message;
import java.util.Set;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin implements DimensionTravel {

    @Shadow
    private @Nullable Vec3 enteredNetherPosition;

    @Unique
    private boolean disabledimensionsreimagined$allowed = false;

    @Inject(method = "changeDimension", at = @At("HEAD"), cancellable = true)
    private void disabledimensionsreimagined$disableDimensionTravel(
        final DimensionTransition transition,
        CallbackInfoReturnable<Entity> callback
    ) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        ServerLevel currentLevel = player.serverLevel();
        ServerLevel newLevel = transition.newLevel();

        if (newLevel.dimension() == currentLevel.dimension()) {
            return;
        }

        if (this.disabledimensionsreimagined$allowed) {
            return;
        }

        Dimension dimension = Common.getConfig().getDimension(
            newLevel.dimension().location()
        );
        if (dimension == null) {
            return;
        }

        if (!dimension.isDisabled(player)) {
            return;
        }

        disabledimensionsreimagined$applyVfx(player, dimension.message);

        callback.setReturnValue(null);
    }

    public void disabledimensionsreimagined$checkTeleport(
        ResourceKey<Level> from,
        ResourceKey<Level> to
    ) {
        ServerPlayer player = (ServerPlayer) (Object) this;

        Dimension dimension = Common.getConfig().getDimension(to.location());
        if (dimension == null) {
            return;
        }

        if (!dimension.isDisabled(player)) {
            return;
        }

        if (to.equals(Level.NETHER) && this.enteredNetherPosition != null) {
            this.disabledimensionsreimagined$teleportBack(
                player,
                this.enteredNetherPosition,
                player.getYRot(),
                player.getXRot(),
                from,
                dimension.message
            );
        } else {
            DimensionTransition transition =
                player.findRespawnPositionAndUseSpawnBlock(
                    false,
                    DimensionTransition.DO_NOTHING
                );

            this.disabledimensionsreimagined$teleportBack(
                player,
                transition.pos(),
                transition.yRot(),
                transition.xRot(),
                transition.newLevel().dimension(),
                dimension.message
            );
        }
    }

    private void disabledimensionsreimagined$teleportBack(
        ServerPlayer player,
        Vec3 position,
        float yRot,
        float xRot,
        ResourceKey<Level> to,
        Message message
    ) {
        if (this.disabledimensionsreimagined$allowed) {
            return;
        }

        ServerLevel returnLevel = player.level().getServer().getLevel(to);
        if (returnLevel == null) {
            return;
        }

        this.disabledimensionsreimagined$allowed = true;

        try {
            player.teleportTo(
                returnLevel,
                position.x,
                position.y,
                position.z,
                Set.of(),
                yRot,
                xRot
            );
        } finally {
            this.disabledimensionsreimagined$allowed = false;
            this.disabledimensionsreimagined$applyVfx(player, message);
        }
    }

    private void disabledimensionsreimagined$applyVfx(
        ServerPlayer player,
        Message message
    ) {
        player.displayClientMessage(
            Component.literal(message.text).withColor(
                TextColor.parseColor(message.color).result().get().getValue()
            ),
            true
        );

        player.addEffect(
            new MobEffectInstance(
                MobEffects.MOVEMENT_SLOWDOWN,
                20,
                5,
                true,
                false,
                false
            )
        );

        player.connection.send(
            new ClientboundSoundEntityPacket(
                SoundEvents.NOTE_BLOCK_BASS,
                SoundSource.MASTER,
                player,
                1.0F,
                0.5F,
                player.getRandom().nextLong()
            )
        );
    }
}
