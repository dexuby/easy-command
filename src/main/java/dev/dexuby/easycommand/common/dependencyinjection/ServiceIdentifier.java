package dev.dexuby.easycommand.common.dependencyinjection;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ServiceIdentifier {

    private final Class<?> clazz;
    private final String identifier;

    public ServiceIdentifier(final Class<?> clazz) {

        this.clazz = clazz;
        this.identifier = clazz.getName();

    }

    public ServiceIdentifier(@NotNull final Class<?> clazz,
                             @NotNull final String identifier) {

        this.clazz = clazz;
        this.identifier = identifier;

    }

    @Override
    public boolean equals(final Object input) {

        if (this == input) return true;
        if (input == null || getClass() != input.getClass()) return false;

        final ServiceIdentifier serviceIdentifier = (ServiceIdentifier) input;
        return clazz.equals(serviceIdentifier.clazz) && identifier.equals(serviceIdentifier.identifier);

    }

    @Override
    public int hashCode() {

        return Objects.hash(clazz, identifier);

    }

    public static ServiceIdentifier of(@NotNull final Class<?> clazz) {

        return new ServiceIdentifier(clazz);

    }

}
