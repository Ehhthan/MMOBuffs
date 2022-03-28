package com.ehhthan.mmobuffs.comp.stat;

import com.ehhthan.mmobuffs.api.EffectHolder;
import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import com.ehhthan.mmobuffs.api.stat.StatKey;
import com.ehhthan.mmobuffs.api.stat.StatValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StatHandler<P> {
    @NotNull String namespace();

    @Nullable P adapt(@NotNull EffectHolder holder);

    void add(@NotNull EffectHolder holder, @NotNull ActiveStatusEffect effect, @NotNull StatKey key, @NotNull StatValue value);

    void remove(@NotNull EffectHolder holder, @NotNull StatKey key);

    @NotNull String getValue(@NotNull EffectHolder holder, @NotNull StatKey key);
}
