package dev.dexuby.easycommand.common;

import org.jetbrains.annotations.NotNull;

public interface FluentBuilder<T> {

    @NotNull
    T build();

}
