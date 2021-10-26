package com.ehhthan.mmobuffs.api.tag.custom;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import com.ehhthan.mmobuffs.api.effect.StatusEffect;
import com.ehhthan.mmobuffs.api.tag.CustomTagTypes;
import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

import static com.ehhthan.mmobuffs.api.tag.CustomTagTypes.*;

public class StatusEffectTag implements PersistentDataType<PersistentDataContainer, ActiveStatusEffect> {
    private static final NamespacedKey EFFECT = key("effect");
    private static final NamespacedKey DURATION = key("duration");
    private static final NamespacedKey STACKS = key("stacks");
    private static final NamespacedKey SHOW_DISPLAY = key("showDisplay");

    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<ActiveStatusEffect> getComplexType() {
        return ActiveStatusEffect.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull ActiveStatusEffect effect, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer container = context.newPersistentDataContainer();

        container.set(EFFECT, STRING, effect.getEffect().getKey());

        if (effect.getDuration() != -1)
            container.set(DURATION, INTEGER, effect.getDuration());

        container.set(STACKS, INTEGER, effect.getStacks());
        container.set(SHOW_DISPLAY, BOOLEAN, effect.isShowDisplay());

        return container;
    }

    @Override
    public @NotNull ActiveStatusEffect fromPrimitive(@NotNull PersistentDataContainer container, @NotNull PersistentDataAdapterContext context) {
        StatusEffect effect = MMOBuffs.getInst().getEffectManager().get(container.getOrDefault(EFFECT, STRING, ""));
        int duration = container.getOrDefault(DURATION, INTEGER, -1);
        int stacks = container.getOrDefault(STACKS, INTEGER, 0);
        boolean showDisplay = container.getOrDefault(SHOW_DISPLAY, BOOLEAN, true);
        return new ActiveStatusEffect(effect, duration, stacks, showDisplay);
    }

    private static NamespacedKey key(String key) {
        return new NamespacedKey(MMOBuffs.getInst(), key);
    }
}
