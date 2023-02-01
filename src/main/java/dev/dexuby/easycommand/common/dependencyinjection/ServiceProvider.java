package dev.dexuby.easycommand.common.dependencyinjection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ServiceProvider {

    <T> void addService(@NotNull final T service);

    <T> void addService(@NotNull final Class<T> clazz, @NotNull final T service);

    <T> void addService(@NotNull final String identifier, @NotNull final T service);

    <T> void addService(@NotNull final Class<T> clazz, @NotNull final String identifier, @NotNull final T service);

    @Nullable <T> T getService(@NotNull final Class<T> clazz);

    @Nullable <T> T getService(@NotNull final Class<T> clazz, @NotNull final String identifier);

    boolean hasService(@NotNull final Class<?> clazz);

    boolean hasService(@NotNull final Class<?> clazz, @NotNull final String identifier);

}
