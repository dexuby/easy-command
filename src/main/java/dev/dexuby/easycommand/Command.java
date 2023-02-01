package dev.dexuby.easycommand;

import org.jetbrains.annotations.NotNull;

public abstract class Command {

    @NotNull
    abstract String getFirstValue();

    @NotNull
    abstract String getSecondValue();

}
