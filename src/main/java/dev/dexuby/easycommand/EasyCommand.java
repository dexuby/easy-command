package dev.dexuby.easycommand;

import dev.dexuby.easycommand.common.FluentBuilder;
import dev.dexuby.easycommand.common.dependencyinjection.InstanceServiceProvider;
import dev.dexuby.easycommand.common.dependencyinjection.ServiceProvider;
import dev.dexuby.easyreflect.EasyReflect;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EasyCommand {

    private final ServiceProvider serviceProvider = new InstanceServiceProvider();

    private ClassLoader classLoader;
    private EasyReflect easyReflect;

    private EasyCommand() {

    }

    public ServiceProvider getServiceProvider() {

        return this.serviceProvider;

    }

    public List<Command> findAndRegisterCommands() {

        final List<Command> instances = new ArrayList<>();
        final Map<Class<?>, CommandHandler> classes = this.easyReflect.findAnnotatedClasses(CommandHandler.class);
        for (final Map.Entry<Class<?>, CommandHandler> entry : classes.entrySet()) {
            try {
                if (!Command.class.isAssignableFrom(entry.getKey())) continue;
                final Constructor<?>[] constructors = entry.getKey().getDeclaredConstructors();
                assert constructors.length >= 1;

                Command instance;
                final Constructor<?> constructor = constructors[0];
                if (constructor.getParameters().length > 0) {
                    final Object[] parameters = new Object[constructor.getParameters().length];
                    for (int i = 0; i < constructor.getParameters().length; i++) {
                        final Parameter parameter = constructor.getParameters()[i];
                        if (parameter.isAnnotationPresent(Identifier.class)) {
                            final Identifier identifier = parameter.getAnnotation(Identifier.class);
                            parameters[i] = this.serviceProvider.getService(parameter.getType(), identifier.value());
                        } else {
                            parameters[i] = this.serviceProvider.getService(parameter.getType());
                        }
                    }
                    instance = (Command) constructor.newInstance(parameters);
                } else {
                    instance = (Command) constructor.newInstance();
                }
                instances.add(instance);
            } catch (final InvocationTargetException | InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

        return instances;

    }

    public static class Builder implements FluentBuilder<EasyCommand> {

        private final EasyCommand easyCommand = new EasyCommand();
        private final List<String> targetPackages = new ArrayList<>();
        private final List<String> ignoredPackages = new ArrayList<>();

        public Builder classLoader(@NotNull final ClassLoader classLoader) {

            this.easyCommand.classLoader = classLoader;
            return this;

        }

        public <T> Builder ignorePackage(@NotNull final String packageName) {

            this.ignoredPackages.add(packageName);
            return this;

        }

        public <T> Builder resolvePackage(@NotNull final String packageName) {

            this.targetPackages.add(packageName);
            return this;

        }

        public <T> Builder service(@NotNull final T instance) {

            this.easyCommand.serviceProvider.addService(instance);
            return this;

        }

        public <T> Builder service(@NotNull final String identifier, @NotNull final T instance) {

            this.easyCommand.serviceProvider.addService(identifier, instance);
            return this;

        }

        @NotNull
        @Override
        public EasyCommand build() {

            assert this.easyCommand.classLoader != null;

            this.easyCommand.easyReflect = new EasyReflect();
            for (final String ignoredPackage : this.ignoredPackages)
                this.easyCommand.easyReflect.ignorePackage(ignoredPackage);
            for (final String targetPackage : this.targetPackages)
                this.easyCommand.easyReflect.resolvePackage(this.easyCommand.classLoader, targetPackage);

            return this.easyCommand;

        }

    }

}
