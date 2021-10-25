package com.ehhthan.mmobuffs.api.tag.custom;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.effect.duration.EffectDuration;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class EffectDurationTag implements PersistentDataType<PersistentDataContainer, EffectDuration> {
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<EffectDuration> getComplexType() {
        return EffectDuration.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull EffectDuration duration, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer container = context.newPersistentDataContainer();
        container.set(key("creation"), LONG, duration.getCreation().getEpochSecond());
        container.set(key("seconds"), INTEGER, duration.getSeconds());
        return container;
    }

    @Override
    public @NotNull EffectDuration fromPrimitive(@NotNull PersistentDataContainer container, @NotNull PersistentDataAdapterContext context) {

    }

    private static NamespacedKey key(String key) {
        return new NamespacedKey(MMOBuffs.getInst(), key);
    }
}
