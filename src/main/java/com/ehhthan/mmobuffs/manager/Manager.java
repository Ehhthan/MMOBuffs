package com.ehhthan.mmobuffs.manager;

import com.ehhthan.mmobuffs.MMOBuffs;

import java.util.Collection;
import java.util.logging.Level;

public abstract class Manager<T> {
    protected final String name;

    public Manager() {
        this.name = getClass().getSimpleName();
    }

    public String getName() {
        return name;
    }

    public abstract Collection<T> values();

    public abstract void clear();

    public abstract int size();

    public void registerAll(Collection<T> properties) {
        properties.forEach(this::register);
    }

    @SafeVarargs
    public final void registerAll(T... properties) {
        for (T property : properties)
            register(property);
    }

    public abstract void register(T property);

    public void error(String key, Exception e) {
        MMOBuffs.getInst().getLogger().log(Level.WARNING, name + " Error: " + "Could not load '" + key + "' -> " + e.getMessage());
    }
}
