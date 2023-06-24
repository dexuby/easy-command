package dev.dexuby.easycommand.common;

import dev.dexuby.easycommon.builder.FluentBuilder;
import dev.dexuby.easycommon.conditional.Conditional;
import dev.dexuby.easycommon.conditional.Preconditions;
import dev.dexuby.easycommon.dependencyinjection.InstanceServiceProvider;
import dev.dexuby.easycommon.dependencyinjection.ServiceProvider;
import dev.dexuby.easyreflect.EasyReflect;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EasyCommand {

    private ClassLoader classLoader;
    private EasyReflect easyReflect;
    private ServiceProvider serviceProvider;

    private EasyCommand() {

        this.classLoader = ClassLoader.getSystemClassLoader();
        this.easyReflect = new EasyReflect();
        this.serviceProvider = InstanceServiceProvider.builder()
                .selfService()
                .build();

    }

    public ServiceProvider getServiceProvider() {

        return this.serviceProvider;

    }

    /**
     * Resolves all command handlers and initializes a new instance using the first found satisfiable constructor
     * prioritising constructors with no parameters.
     *
     * @return The resolved commands.
     */

    public List<Command> findAndCreateCommands() {

        final Map<Class<?>, CommandHandler> classes = this.easyReflect.findAnnotatedClasses(CommandHandler.class);
        final List<Command> instances = new ArrayList<>(classes.size());
        for (final Map.Entry<Class<?>, CommandHandler> entry : classes.entrySet()) {
            try {
                if (!Command.class.isAssignableFrom(entry.getKey())) continue;
                final Constructor<?>[] constructors = entry.getKey().getConstructors();
                Preconditions.checkState(constructors.length >= 1);

                Constructor<?> targetConstructor = null;
                for (final Constructor<?> constructor : constructors) {
                    // Select constructor without params if present.
                    if (constructor.getParameterCount() == 0) {
                        targetConstructor = constructor;
                        break;
                    }
                }

                Object[] resolvedParameters = new Object[0];
                if (targetConstructor == null) {
                    // Find a dependency satisfiable constructor and resolve.
                    for (final Constructor<?> constructor : constructors) {
                        boolean satisfied = true;
                        final Parameter[] parameters = constructor.getParameters();
                        for (final Parameter parameter : parameters) {
                            if (parameter.isAnnotationPresent(Identifier.class)) {
                                final Identifier identifier = parameter.getAnnotation(Identifier.class);
                                if (!this.serviceProvider.hasService(parameter.getType(), identifier.value())) {
                                    satisfied = false;
                                    break;
                                }
                            } else {
                                if (!this.serviceProvider.hasService(parameter.getType())) {
                                    satisfied = false;
                                    break;
                                }
                            }
                        }

                        if (satisfied) {
                            targetConstructor = constructor;
                            resolvedParameters = new Object[parameters.length];
                            for (int i = 0; i < parameters.length; i++) {
                                final Parameter parameter = parameters[i];
                                if (parameter.isAnnotationPresent(Identifier.class)) {
                                    final Identifier identifier = parameter.getAnnotation(Identifier.class);
                                    resolvedParameters[i] = this.serviceProvider.getService(parameter.getType(), identifier.value());
                                } else {
                                    resolvedParameters[i] = this.serviceProvider.getService(parameter.getType());
                                }
                            }
                        }
                    }
                }

                if (targetConstructor == null) {
                    // Failed to find constructor, skipping...
                } else {
                    instances.add((Command) targetConstructor.newInstance(resolvedParameters));
                }
            } catch (final InvocationTargetException | InstantiationException | IllegalAccessException | IllegalStateException ex) {
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

        private final List<String> ignoredPackages = new LinkedList<>();
        private final List<String> targetPackages = new LinkedList<>();

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
