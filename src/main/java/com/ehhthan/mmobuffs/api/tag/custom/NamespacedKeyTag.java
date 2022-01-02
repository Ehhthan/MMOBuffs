package com.ehhthan.mmobuffs.api.tag.custom;

import com.google.common.base.Preconditions;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class NamespacedKeyTag implements PersistentDataType<String, NamespacedKey> {
    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<NamespacedKey> getComplexType() {
        return NamespacedKey.class;
    }

    @Override
    public @NotNull String toPrimitive(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        return namespacedKey.asString();
    }

    @Override
    public @NotNull NamespacedKey fromPrimitive(@NotNull String s, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        NamespacedKey key = NamespacedKey.fromString(s);
        Preconditions.checkNotNull(key, "NamespacedKey is null: %s", s);

        return key;
    }
}