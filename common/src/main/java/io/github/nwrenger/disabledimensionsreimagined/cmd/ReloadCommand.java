package io.github.nwrenger.disabledimensionsreimagined.cmd;

import io.github.nwrenger.disabledimensionsreimagined.Common;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class ReloadCommand {

    private ReloadCommand() {}

    public static int run(CommandSourceStack source) {
        // Reload and catch + send errors
        try {
            Common.loadConfig();
        } catch (Exception exception) {
            String error = Objects.toString(
                exception.getMessage(),
                exception.getClass().getSimpleName()
            ).replace("[Disable Dimensions Reimagined] ", "");

            source.sendFailure(failure(error));
            return 0;
        }

        // No errors, send success message
        source.sendSuccess(ReloadCommand::success, false);
        return 1;
    }

    private static Component success() {
        return Command.header("command.reload.title", "Reload")
            .append(
                Command.translation(
                    "command.reload.success",
                    "Successful"
                ).withStyle(ChatFormatting.GREEN)
            )
            .append("\n");
    }

    private static Component failure(String error) {
        return Command.header("command.reload.title", "Reload")
            .append(
                Command.translation("command.reload.failure", "Failed")
                    .withStyle(ChatFormatting.RED)
                    .append("\n")
                    .append(
                        Component.literal(error).withStyle(ChatFormatting.GRAY)
                    )
            )
            .append("\n");
    }
}
