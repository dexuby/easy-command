package dev.dexuby.easycommand;

import org.jetbrains.annotations.NotNull;

@CommandHandler
public class TestCommand extends Command {

    private final String firstValue;
    private final String secondValue;

    public TestCommand(@NotNull @Identifier("third") final String firstValue,
                       @NotNull final String secondValue) {

        this.firstValue = firstValue;
        this.secondValue = secondValue;

    }

    @NotNull
    @Override
    public String getFirstValue() {

        return this.firstValue;

    }

    @NotNull
    @Override
    public String getSecondValue() {

        return this.secondValue;

    }

}
