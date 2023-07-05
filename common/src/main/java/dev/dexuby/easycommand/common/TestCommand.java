package dev.dexuby.easycommand.common;

import org.jetbrains.annotations.NotNull;

public class TestCommand {

    private Class<?> parent;
    private String name;

    public TestCommand(@NotNull final Class<?> parent,
                       @NotNull final String name) {

        this.parent = parent;
        this.name = name;

    }

    public Class<?> getParent() {

        return this.parent;

    }

    public String getName() {

        return this.name;

    }

}
