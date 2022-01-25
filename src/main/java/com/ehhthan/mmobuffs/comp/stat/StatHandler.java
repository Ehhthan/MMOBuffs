package com.ehhthan.mmobuffs.comp.stat;

import com.ehhthan.mmobuffs.api.EffectHolder;
import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;

public interface StatHandler<T> {
    void edit(EffectHolder holder, EditType type, ActiveStatusEffect effect);

    T adapt(EffectHolder holder);

    String getValue(EffectHolder holder, String key);

    enum EditType {
        ADD,
        REMOVE
    }
}
