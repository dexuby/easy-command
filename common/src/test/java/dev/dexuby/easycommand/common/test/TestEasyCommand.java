package dev.dexuby.easycommand.common.test;

import dev.dexuby.easycommand.common.Command;
import dev.dexuby.easycommand.common.EasyCommand;
import dev.dexuby.easycommon.dependencyinjection.InstanceServiceProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestEasyCommand {

    @Test
    public void test() {

        final EasyCommand easyCommand = EasyCommand.builder()
                .classLoader(ClassLoader.getSystemClassLoader())
                .serviceProvider(InstanceServiceProvider.builder()
                        .service("first", "hello world")
                        .service("second", "test")
                        .service("second value no identifier")
                        .selfService()
                        .build())
                .resolvePackage("dev.dexuby.easycommand")
                .build();

        easyCommand.getServiceProvider().addService("third", "late service");

        final List<Command> instances = easyCommand.findAndCreateCommands();
        assertEquals(1, instances.size());

        final Command firstInstance = instances.get(0);
        assertEquals("late service", firstInstance.getFirstValue());
        assertEquals("second value no identifier", firstInstance.getSecondValue());

    }

}
