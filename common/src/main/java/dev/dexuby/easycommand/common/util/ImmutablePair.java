package dev.dexuby.easycommand.common.util;

import java.util.Objects;

public class ImmutablePair<K, V> implements KeyValuePair<K, V> {

    public final K key;
    public final V value;

    public ImmutablePair(final K key, final V value) {

        this.key = key;
        this.value = value;

    }

    @Override
    public K getKey() {

        return key;

    }

    @Override
    public V getValue() {

        return value;

    }

    @Override
    public int hashCode() {

        return Objects.hash(key, value);

    }

    @Override
    public boolean equals(final Object input) {

        if (this == input) return true;
        if (input == null || getClass() != input.getClass()) return false;

        ImmutablePair<?, ?> that = (ImmutablePair<?, ?>) input;
        return Objects.equals(key, that.key) && Objects.equals(value, that.value);

    }

    public static <K, V> ImmutablePair<K, V> of(final K key, final V value) {

        return new ImmutablePair<>(key, value);

    }

}
