package io.github.nwrenger.disabledimensionsreimagined.cmd;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jspecify.annotations.NonNull;

public final class AboutCommand {

    private static final String ISSUES_URL =
        "https://github.com/nwrenger/disable-dimensions-reimagined/issues";

    private AboutCommand() {}

    public static int run(CommandSourceStack source) {
        source.sendSuccess(AboutCommand::message, false);
        return 1;
    }

    private static Component message() {
        return Command.header()
            .append(
                bullet(
                    Command.translation(
                        "command.about.other_projects",
                        "Check my other projects on"
                    )
                        .withStyle(ChatFormatting.GRAY)
                        .append(" ")
                        .append(link(Command.WEBSITE_URL, Command.WEBSITE_URL))
                )
            )

            .append(
                bullet(
                    Command.translation(
                        "command.about.issue_prefix",
                        "If you have an issue with this mod, report it"
                    )
                        .withStyle(ChatFormatting.GRAY)
                        .append(" ")
                        .append(
                            link(
                                Command.translation(
                                    "command.about.issue_link",
                                    "here"
                                ),
                                ISSUES_URL
                            )
                        )
                )
            )

            .append(
                bullet(
                    Command.translation(
                        "command.about.config",
                        "Review the active config with"
                    )
                        .withStyle(ChatFormatting.GRAY)
                        .append(" ")
                        .append(command("/ddr config"))
                )
            )

            .append(
                bullet(
                    Command.translation(
                        "command.about.reload",
                        "Reload the config changes with"
                    )
                        .withStyle(ChatFormatting.GRAY)
                        .append(" ")
                        .append(command("/ddr reload"))
                )
            )

            .append(
                bullet(
                    Command.translation(
                        "command.about.toggle",
                        "Toggle the status of a dimension with"
                    )
                        .withStyle(ChatFormatting.GRAY)
                        .append(" ")
                        .append(
                            suggest("/ddr toggle <dimension>", "/ddr toggle ")
                        )
                )
            );
    }

    @NonNull
    private static MutableComponent bullet(Component component) {
        return Component.literal("▸ ")
            .withStyle(ChatFormatting.DARK_GRAY)
            .append(component)
            .append("\n");
    }

    @NonNull
    private static MutableComponent link(
        @NonNull Component text,
        String url
    ) {
        return text.copy().withStyle(style ->
            style
                .withItalic(true)
                .withColor(ChatFormatting.AQUA)
                .withClickEvent(Command.openUrl(url))
        );
    }

    @NonNull
    private static MutableComponent link(@NonNull String text, String url) {
        return link(Component.literal(text), url);
    }

    @NonNull
    private static MutableComponent command(@NonNull String command) {
        return Component.literal(command).withStyle(style ->
            style
                .withItalic(true)
                .withColor(ChatFormatting.AQUA)
                .withClickEvent(runCommand(command))
        );
    }

    @NonNull
    private static MutableComponent suggest(
        @NonNull String text,
        @NonNull String command
    ) {
        return Component.literal(text).withStyle(style ->
            style
                .withItalic(true)
                .withColor(ChatFormatting.AQUA)
                .withClickEvent(suggestCommand(command))
        );
    }

    @NonNull
    private static ClickEvent runCommand(@NonNull String command) {
        return new ClickEvent.RunCommand(command);
    }

    @NonNull
    private static ClickEvent suggestCommand(@NonNull String command) {
        return new ClickEvent.SuggestCommand(command);
    }
}
