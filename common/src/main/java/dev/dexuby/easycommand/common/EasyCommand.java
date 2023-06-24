package dev.dexuby.easycommand.common;

import dev.dexuby.easycommand.common.util.Conditional;
import dev.dexuby.easycommand.common.util.FluentBuilder;
import dev.dexuby.easycommand.common.dependencyinjection.InstanceServiceProvider;
import dev.dexuby.easycommand.common.dependencyinjection.ServiceProvider;
import dev.dexuby.easycommand.common.util.Preconditions;
import dev.dexuby.easyreflect.EasyReflect;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EasyCommand {

    private ClassLoader classLoader;
    private EasyReflect easyReflect;
    private ServiceProvider serviceProvider;

    private EasyCommand() {

        this.classLoader = this.getClass().getClassLoader();
        this.easyReflect = new EasyReflect();
        this.serviceProvider = new InstanceServiceProvider();

    }

    public ServiceProvider getServiceProvider() {

        return this.serviceProvider;

    }

    public List<Command> findAndCreateCommands() {

        final Map<Class<?>, CommandHandler> classes = this.easyReflect.findAnnotatedClasses(CommandHandler.class);
        final List<Command> instances = new ArrayList<>(classes.size());
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

    private void setClassLoader(@NotNull final ClassLoader classLoader) {

        this.classLoader = classLoader;

    }

    private void setServiceProvider(@NotNull final ServiceProvider serviceProvider) {

        this.serviceProvider = serviceProvider;

    }

    private void setEasyReflect(@NotNull final EasyReflect easyReflect) {

        this.easyReflect = easyReflect;

    }

    public static EasyCommand.Builder builder() {

        return new EasyCommand.Builder();

    }

    public static class Builder implements FluentBuilder<EasyCommand> {

        private final List<String> targetPackages = new ArrayList<>();
        private final List<String> ignoredPackages = new ArrayList<>();

        private ClassLoader classLoader;
        private EasyReflect easyReflect;
        private ServiceProvider serviceProvider;

        public Builder classLoader(@NotNull final ClassLoader classLoader) {

            this.classLoader = classLoader;
            return this;

        }

        public Builder ignorePackage(@NotNull final String packageName) {

            this.ignoredPackages.add(packageName);
            return this;

        }

        public Builder resolvePackage(@NotNull final String packageName) {

            this.targetPackages.add(packageName);
            return this;

        }

        public Builder serviceProvider(@NotNull final ServiceProvider serviceProvider) {

            this.serviceProvider = serviceProvider;
            return this;

        }

        public Builder easyReflect(@NotNull final EasyReflect easyReflect) {

            this.easyReflect = easyReflect;
            return this;

        }

        @NotNull
        @Override
        public EasyCommand build() {

            final EasyCommand easyCommand = new EasyCommand();
            Conditional.executeIfNotNull(this.classLoader, () -> easyCommand.setClassLoader(this.classLoader));
            Conditional.executeIfNotNull(this.serviceProvider, () -> easyCommand.setServiceProvider(this.serviceProvider));
            Conditional.executeIfNotNull(this.easyReflect, () -> easyCommand.setEasyReflect(this.easyReflect));

            for (final String ignoredPackage : this.ignoredPackages)
                easyCommand.easyReflect.ignorePackage(ignoredPackage);
            for (final String targetPackage : this.targetPackages)
                easyCommand.easyReflect.resolvePackage(easyCommand.classLoader, targetPackage);

            return easyCommand;

        }

    }

}
