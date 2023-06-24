package dev.dexuby.easycommand.common.util;

import java.util.Objects;

public class Pair<K, V> implements KeyValuePair<K, V> {

    private K key;
    private V value;

    public Pair(final K key, final V value) {

        this.key = key;
        this.value = value;

    }

    @Override
    public K getKey() {

        return key;

    }

    public void setKey(final K key) {

        this.key = key;

    }

    @Override
    public V getValue() {

        return value;

    }

    public void setValue(final V value) {

        this.value = value;

    }

    @Override
    public int hashCode() {

        return Objects.hash(key, value);

    }

    @Override
    public boolean equals(final Object input) {

        if (this == input) return true;
        if (input == null || getClass() != input.getClass()) return false;

        Pair<?, ?> that = (Pair<?, ?>) input;
        return Objects.equals(key, that.key) && Objects.equals(value, that.value);

    }

    public static <K, V> Pair<K, V> of(final K key, final V value) {

        return new Pair<>(key, value);

    }

}
