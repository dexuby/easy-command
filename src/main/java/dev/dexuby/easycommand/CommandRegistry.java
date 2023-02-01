package dev.dexuby.easycommand;

import org.jetbrains.annotations.NotNull;

public interface CommandRegistry {

    void registerCommand(@NotNull final Command command);

}
