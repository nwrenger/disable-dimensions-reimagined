package io.github.nwrenger.disabledimensionsreimagined.config;

import net.minecraft.network.chat.TextColor;
import org.jspecify.annotations.Nullable;

public class Message {

    public String text;
    public String color;

    public Message(String text, String color) {
        this.text = text;
        this.color = color;
    }

    public static void validate(@Nullable Message message) {
        if (message == null) {
            throw new IllegalStateException(
                "[Disable Dimensions Reimagined] `message` must not be null"
            );
        }

        if (message.text == null) {
            throw new IllegalStateException(
                "[Disable Dimensions Reimagined] `message.text` must not be null"
            );
        }

        if (
            message.color == null ||
            TextColor.parseColor(message.color).result().isEmpty()
        ) {
            throw new IllegalStateException(
                "[Disable Dimensions Reimagined] Invalid message color: " +
                    message.color
            );
        }
    }
}
