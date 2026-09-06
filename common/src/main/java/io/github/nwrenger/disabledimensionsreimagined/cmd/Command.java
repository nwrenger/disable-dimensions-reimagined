package io.github.nwrenger.disabledimensionsreimagined.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.nwrenger.disabledimensionsreimagined.Constants;
import java.net.URI;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class Command {

    public static final String WEBSITE_URL = "https://nwrenger.dev";

    public static final int DIMENSION_COLOR = 0xD414E5;

    private Command() {}

    public static void register(
        CommandDispatcher<CommandSourceStack> dispatcher
    ) {
        dispatcher.register(createRoot("ddr"));
        dispatcher.register(createRoot("disabledimensionsreimagined"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> createRoot(
        @NonNull String name
    ) {
        return Commands.literal(name)
            .requires(source -> source.hasPermission(2))
            .then(
                Commands.literal("about").executes(context ->
                    AboutCommand.run(context.getSource())
                )
            )
            .then(
                Commands.literal("config").executes(context ->
                    ConfigCommand.run(context.getSource())
                )
            )
            .then(ToggleCommand.create())
            .then(
                Commands.literal("reload").executes(context ->
                    ReloadCommand.run(context.getSource())
                )
            );
    }

    public static MutableComponent translation(
        String key,
        String fallback,
        Object... arguments
    ) {
        return Component.translatableWithFallback(
            Constants.MOD_ID + "." + key,
            fallback,
            arguments
        );
    }

    public static MutableComponent header() {
        return header(null);
    }

    public static MutableComponent header(String key, String fallback) {
        return header(translation(key, fallback));
    }

    private static MutableComponent header(@Nullable Component title) {
        MutableComponent h = Component.literal("\n")
            .append(
                translation(
                    "command.header",
                    "%1$s by %2$s",
                    Component.literal(Constants.MOD_NAME).withStyle(style ->
                        style.withBold(true).withColor(ChatFormatting.GOLD)
                    ),
                    Component.literal("nwrenger").withStyle(style ->
                        style
                            .withItalic(true)
                            .withColor(TextColor.fromRgb(0xF223F2))
                            .withClickEvent(
                                Command.openUrl(Command.WEBSITE_URL)
                            )
                    )
                )
            )
            .withStyle(ChatFormatting.WHITE);

        if (title != null) {
            h.append(
                Component.literal(" - ")
                    .withStyle(ChatFormatting.WHITE)
                    .append(title.copy().withStyle(ChatFormatting.WHITE))
            );
        }

        h.append("\n\n");

        return h;
    }

    public static MutableComponent entry(
        Component name,
        @NonNull Component value
    ) {
        return Component.literal("▸ ")
            .withStyle(ChatFormatting.DARK_GRAY)
            .append(
                name
                    .copy()
                    .withStyle(ChatFormatting.GRAY)
                    .append(
                        Component.literal(": ").withStyle(ChatFormatting.GRAY)
                    )
            )
            .append(value);
    }

    @NonNull
    public static ClickEvent openUrl(String url) {
        return new ClickEvent.OpenUrl(Objects.requireNonNull(URI.create(url)));
    }
}
