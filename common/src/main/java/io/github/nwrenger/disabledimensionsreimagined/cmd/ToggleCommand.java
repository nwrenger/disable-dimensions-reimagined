package io.github.nwrenger.disabledimensionsreimagined.cmd;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.nwrenger.disabledimensionsreimagined.Common;
import io.github.nwrenger.disabledimensionsreimagined.config.Config;
import io.github.nwrenger.disabledimensionsreimagined.config.Dimension;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

public final class ToggleCommand {

    private static final SuggestionProvider<CommandSourceStack> DIMENSIONS = (
        context,
        builder
    ) ->
        SharedSuggestionProvider.suggestResource(
            Common.getConfig()
                .dimensions.keySet()
                .stream()
                .map(Identifier::parse),
            builder
        );

    private ToggleCommand() {}

    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("toggle").then(
            Commands.argument("dimension", IdentifierArgument.id())
                .suggests(DIMENSIONS)
                .executes(context ->
                    toggle(
                        context.getSource(),
                        IdentifierArgument.getId(context, "dimension")
                    )
                )
        );
    }

    private static int toggle(CommandSourceStack source, Identifier id) {
        Dimension dimension = Common.getConfig().getDimension(id);
        if (dimension == null) {
            return notConfigured(source, id);
        }

        return setDisabled(source, id, !dimension.disabled);
    }

    private static int setDisabled(
        CommandSourceStack source,
        Identifier id,
        boolean disabled
    ) {
        Config config = Common.getConfig();
        Dimension dimension = config.getDimension(id);

        if (dimension == null) {
            return notConfigured(source, id);
        }

        boolean previousDisabled = dimension.disabled;
        dimension.disabled = disabled;

        try {
            config.save();
        } catch (RuntimeException exception) {
            dimension.disabled = previousDisabled;
            source.sendFailure(
                Command.header("command.toggle.title", "Toggle")
                    .append(
                        Command.translation(
                            "command.toggle.save_failure",
                            "Unable to save the configuration"
                        ).withStyle(ChatFormatting.RED)
                    )
                    .append("\n")
            );
            return 0;
        }

        source.sendSuccess(() -> success(id, disabled), false);
        return 1;
    }

    private static int notConfigured(CommandSourceStack source, Identifier id) {
        source.sendFailure(
            Command.header("command.toggle.title", "Toggle")
                .append(
                    Command.translation(
                        "command.toggle.not_configured",
                        "%1$s is not configured yet",
                        Component.literal(id.toString()).withColor(
                            Command.DIMENSION_COLOR
                        )
                    ).withStyle(ChatFormatting.RED)
                )
                .append("\n")
        );
        return 0;
    }

    private static Component success(Identifier id, boolean disabled) {
        MutableComponent message = Command.header(
            "command.toggle.title",
            "Toggle"
        )
            .append(ConfigCommand.dimensionStatus(id.toString(), disabled))
            .append("\n");

        return message;
    }
}
