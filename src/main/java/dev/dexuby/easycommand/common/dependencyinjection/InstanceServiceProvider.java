package dev.dexuby.easycommand.common.dependencyinjection;

import dev.dexuby.easycommand.common.FluentBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class InstanceServiceProvider implements ServiceProvider {

    private final Map<ServiceIdentifier, Object> instances = new HashMap<>();

    @Override
    public <T> void addService(@NotNull final T instance) {

        this.instances.put(ServiceIdentifier.of(instance.getClass()), instance);

    }

    @Override
    public <T> void addService(@NotNull final Class<T> clazz, @NotNull final T instance) {

        this.instances.put(ServiceIdentifier.of(clazz), instance);

    }

    @Override
    public <T> void addService(@NotNull final String identifier, @NotNull final T instance) {

        this.instances.put(new ServiceIdentifier(instance.getClass(), identifier), instance);

    }

    @Override
    public <T> void addService(@NotNull final Class<T> clazz, @NotNull final String identifier, @NotNull final T instance) {

        this.instances.put(new ServiceIdentifier(clazz, identifier), instance);

    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getService(@NotNull final Class<T> clazz) {

        return (T) this.instances.get(ServiceIdentifier.of(clazz));

    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getService(@NotNull final Class<T> clazz, @NotNull final String identifier) {

        return (T) this.instances.get(new ServiceIdentifier(clazz, identifier));

    }

    @Override
    public boolean hasService(@NotNull final Class<?> clazz) {

        return this.instances.containsKey(ServiceIdentifier.of(clazz));

    }

    @Override
    public boolean hasService(@NotNull final Class<?> clazz, @NotNull final String identifier) {

        return this.instances.containsKey(new ServiceIdentifier(clazz, identifier));

    }

    public static class Builder implements FluentBuilder<InstanceServiceProvider> {

        private final InstanceServiceProvider serviceProvider = new InstanceServiceProvider();

        public <T> Builder service(@NotNull final T instance) {

            this.serviceProvider.addService(instance);
            return this;

        }

        public <T> Builder service(@NotNull final Class<T> clazz, @NotNull final T instance) {

            this.serviceProvider.addService(clazz, instance);
            return this;

        }

        public <T> Builder service(@NotNull final String identifier, @NotNull final T instance) {

            this.serviceProvider.addService(identifier, instance);
            return this;

        }

        public <T> Builder service(@NotNull final Class<T> clazz, @NotNull final String identifier, @NotNull final T instance) {

            this.serviceProvider.addService(clazz, identifier, instance);
            return this;

        }

        @NotNull
        public InstanceServiceProvider build() {

            return this.serviceProvider;

        }

    }

}
