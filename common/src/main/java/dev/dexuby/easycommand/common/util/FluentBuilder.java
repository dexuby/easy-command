package dev.dexuby.easycommand.common.util;

import org.jetbrains.annotations.NotNull;

public interface FluentBuilder<T> {

    @NotNull
    T build();

}
