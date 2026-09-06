package io.github.nwrenger.disabledimensionsreimagined.config;

import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;

public class Dimension {

    public boolean disabled;
    public Message message;
    public Condition[] conditions;

    public Dimension(
        boolean disabled,
        Message message,
        Condition[] conditions
    ) {
        this.disabled = disabled;
        this.message = message;
        this.conditions = conditions;
    }

    public static void validate(@Nullable Dimension dimension) {
        if (dimension == null) {
            throw new IllegalStateException(
                "[Disable Dimensions Reimagined] `dimension` must not be null"
            );
        }

        Message.validate(dimension.message);

        if (dimension.conditions == null) {
            throw new IllegalStateException(
                "[Disable Dimensions Reimagined] `dimension.conditions` must not be null"
            );
        }

        for (Condition condition : dimension.conditions) {
            Condition.validate(condition);
        }
    }

    public boolean isDisabled(Entity entity) {
        for (Condition condition : this.conditions) {
            if (condition.check(entity)) {
                return condition.disabled;
            }
        }

        return this.disabled;
    }
}
