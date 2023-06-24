package dev.dexuby.easycommand.common.util;

import dev.dexuby.easycommon.Procedure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Conditional {

    public static void executeIfTrue(final boolean state, @NotNull final Procedure procedure) {

        if (state) procedure.execute();

    }

    public static <T> void executeIfNotNull(@Nullable final T reference, @NotNull final Procedure procedure) {

        if (reference != null) procedure.execute();

    }

    public static <T> void executeIfNull(@Nullable final T reference, @NotNull final Procedure procedure) {

        if (reference == null) procedure.execute();

    }

}
