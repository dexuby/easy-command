package dev.dexuby.easycommand.common.util;

import org.jetbrains.annotations.Nullable;

public final class Preconditions {

    public static <T> void checkNotNull(@Nullable final T reference) throws NullPointerException {

        if (reference == null)
            throw new NullPointerException("Reference was null.");

    }

    public static <T> void checkState(final boolean state) throws IllegalStateException {

        if (!state)
            throw new IllegalStateException("State was false.");

    }

}
