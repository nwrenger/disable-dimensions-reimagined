package io.github.nwrenger.disabledimensionsreimagined.cmd;

import io.github.nwrenger.disabledimensionsreimagined.Common;
import io.github.nwrenger.disabledimensionsreimagined.config.Condition;
import io.github.nwrenger.disabledimensionsreimagined.config.Config;
import io.github.nwrenger.disabledimensionsreimagined.config.Dimension;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;

public class ConfigCommand {

    private ConfigCommand() {}

    public static int run(CommandSourceStack source) {
        Config config = Common.getConfig();
        source.sendSuccess(() -> message(config), false);
        return 1;
    }

    private static Component message(Config config) {
        MutableComponent message = Command.header(
            "command.config.title",
            "Config"
        );

        if (config.dimensions.isEmpty()) {
            return message
                .append(
                    Command.translation(
                        "command.config.empty",
                        "No dimension entries configured."
                    ).withStyle(ChatFormatting.DARK_GRAY)
                )
                .append("\n");
        }

        int dimensionIndex = 0;
        for (Map.Entry<
            String,
            Dimension
        > entry : config.dimensions.entrySet()) {
            if (dimensionIndex > 0) {
                message.append("\n\n");
            }

            appendDimension(message, entry.getKey(), entry.getValue());
            dimensionIndex++;
        }

        return message.append("\n");
    }

    private static void appendDimension(
        MutableComponent message,
        String id,
        Dimension dimension
    ) {
        message
            .append(Component.literal("▸ ").withStyle(ChatFormatting.DARK_GRAY))
            .append(Component.literal(id).withColor(Command.DIMENSION_COLOR))
            .append("\n")
            .append(
                detailLabel(
                    "command.config.dimension.status.label",
                    "Status"
                )
            )
            .append(
                status(
                    dimension.disabled,
                    dimensionStatus(id, dimension.disabled)
                )
            )
            .append("\n")
            .append(
                detailLabel(
                    "command.config.dimension.message.label",
                    "Message"
                )
            )
            .append(Component.literal("\"").withStyle(ChatFormatting.DARK_GRAY))
            .append(
                Component.literal(dimension.message.text).withColor(
                    TextColor.parseColor(dimension.message.color)
                        .result()
                        .get()
                        .getValue()
                )
            )
            .append(Component.literal("\"").withStyle(ChatFormatting.DARK_GRAY))
            .append("\n")
            .append(
                detailLabel(
                    "command.config.dimension.conditions.label",
                    "Conditions"
                )
            );

        if (dimension.conditions.length == 0) {
            message.append(
                Command.translation(
                    "command.config.dimension.conditions.empty",
                    "None"
                ).withStyle(ChatFormatting.DARK_GRAY)
            );
            return;
        }

        message.append("\n");

        for (int index = 0; index < dimension.conditions.length; index++) {
            if (index > 0) {
                message.append("\n");
            }

            appendCondition(
                message,
                id,
                index + 1,
                dimension.conditions[index]
            );
        }
    }

    private static void appendCondition(
        MutableComponent message,
        String dimensionId,
        int index,
        Condition condition
    ) {
        message
            .append(
                Component.literal("   %d. ".formatted(index)).withStyle(
                    ChatFormatting.DARK_GRAY
                )
            )
            .append(Component.literal("\"").withStyle(ChatFormatting.DARK_GRAY))
            .append(
                Component.literal(condition.value).withStyle(
                    ChatFormatting.AQUA
                )
            )
            .append(
                Component.literal("\" (").withStyle(ChatFormatting.DARK_GRAY)
            )
            .append(
                Component.literal(condition.type.name()).withStyle(
                    ChatFormatting.DARK_GREEN
                )
            )
            .append(Component.literal(")").withStyle(ChatFormatting.DARK_GRAY))
            .append(
                Component.literal(" → ").withStyle(ChatFormatting.DARK_GRAY)
            )
            .append(
                status(
                    condition.disabled,
                    conditionStatus(dimensionId, condition.disabled)
                )
            );
    }

    private static MutableComponent detailLabel(
        String key,
        String fallback
    ) {
        return Component.literal("  ")
            .append(
                Command.translation(key, fallback).withStyle(
                    ChatFormatting.GRAY
                )
            )
            .append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
    }

    private static MutableComponent status(boolean disabled, Component hover) {
        return Component.literal(disabled ? "☒" : "☑").withStyle(style ->
            style
                .withColor(disabled ? ChatFormatting.RED : ChatFormatting.GREEN)
                .withHoverEvent(
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover)
                )
        );
    }

    public static Component dimensionStatus(
        String dimensionId,
        boolean disabled
    ) {
        return Command.translation(
            "command.config.dimension.status.hover",
            "Entering %1$s is %2$s %3$s",
            Component.literal(dimensionId).withColor(Command.DIMENSION_COLOR),
            Command.translation("command.state.now", "now").withStyle(
                ChatFormatting.GOLD
            ),
            statusText(disabled)
        ).withStyle(ChatFormatting.WHITE);
    }

    private static Component conditionStatus(
        String dimensionId,
        boolean disabled
    ) {
        return Command.translation(
            "command.config.condition.status.hover",
            "Entering %1$s will be %2$s",
            Command.translation(
                "command.config.condition.status.context",
                "following this condition"
            ).withStyle(
                    ChatFormatting.GOLD
            ),
            statusText(disabled)
        ).withStyle(ChatFormatting.WHITE);
    }

    private static MutableComponent statusText(boolean disabled) {
        return disabled
            ? Command.translation(
                "command.state.disabled",
                "disabled"
            ).withStyle(
                ChatFormatting.RED
            )
            : Command.translation("command.state.enabled", "enabled").withStyle(
                ChatFormatting.GREEN
            );
    }
}
