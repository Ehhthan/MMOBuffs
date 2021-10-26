package com.ehhthan.mmobuffs.manager;

import org.apache.commons.lang.Validate;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public abstract class KeyedManager<T> extends Manager<T> {
    protected final Map<String, T> managed = new LinkedHashMap<>();

    @Override
    public Collection<T> values() {
        return managed.values();
    }

    @Override
    public void clear() {
        managed.clear();
    }

    @Override
    public int size() {
        return managed.size();
    }

    @Override
    public void register(T property) {
        if (property instanceof Keyable)
            managed.put(((Keyable) property).getKey(), property);
    }

    public void register(String key, T property) {
        managed.put(key, property);
    }

    public void registerAll(Map<String, T> properties) {
        managed.putAll(properties);
    }

    public Collection<String> keys() {
        return managed.keySet();
    }

    public boolean has(String key) {
        return managed.containsKey(Keyable.format(key));
    }

    public T get(String key) {
        Validate.isTrue(has(key), "Effect does not exist.");
        return managed.get(Keyable.format(key));
    }
}
