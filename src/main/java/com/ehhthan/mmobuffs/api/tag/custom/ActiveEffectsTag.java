package com.ehhthan.mmobuffs.api.tag.custom;

import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import com.ehhthan.mmobuffs.api.tag.CustomTagTypes;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ActiveEffectsTag implements PersistentDataType<PersistentDataContainer[], ActiveStatusEffect[]> {
    @Override
    public @NotNull Class<PersistentDataContainer[]> getPrimitiveType() {
        return PersistentDataContainer[].class;
    }

    @Override
    public @NotNull Class<ActiveStatusEffect[]> getComplexType() {
        return ActiveStatusEffect[].class;
    }

    @Override
    public PersistentDataContainer @NotNull [] toPrimitive(ActiveStatusEffect @NotNull [] complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer[] primitive = new PersistentDataContainer[complex.length];

        for (int i = 0; i < complex.length; i++) {
            primitive[i] = CustomTagTypes.ACTIVE_EFFECT.toPrimitive(complex[i], context);

        }

        return primitive;
    }

    @Override
    public ActiveStatusEffect @NotNull [] fromPrimitive(PersistentDataContainer @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
        ActiveStatusEffect[] complex = new ActiveStatusEffect[primitive.length];

        for (int i = 0; i < primitive.length; i++) {
            complex[i] = CustomTagTypes.ACTIVE_EFFECT.fromPrimitive(primitive[i], context);
        }

        return complex;
    }
}
