package dev.dexuby.easycommand.common;

import org.jetbrains.annotations.NotNull;

public interface CommandRegistry {

    void registerCommand(@NotNull final Command command);

}
