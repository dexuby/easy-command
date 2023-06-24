package dev.dexuby.easycommand.common;

import org.jetbrains.annotations.NotNull;

public abstract class Command {

    @NotNull
    public abstract String getFirstValue();

    @NotNull
    public abstract String getSecondValue();

}
