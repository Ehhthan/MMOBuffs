package com.ehhthan.mmobuffs.api.tag;

import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import com.ehhthan.mmobuffs.api.tag.custom.ActiveEffectTag;
import com.ehhthan.mmobuffs.api.tag.custom.ActiveEffectsTag;
import com.ehhthan.mmobuffs.api.tag.custom.BooleanTag;
import com.ehhthan.mmobuffs.api.tag.custom.NamespacedKeyTag;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public interface CustomTagTypes {
    PersistentDataType<Byte, Boolean> BOOLEAN = new BooleanTag();
    PersistentDataType<String, NamespacedKey> NAMESPACED_KEY = new NamespacedKeyTag();

    PersistentDataType<PersistentDataContainer, ActiveStatusEffect> ACTIVE_EFFECT = new ActiveEffectTag();
    PersistentDataType<PersistentDataContainer[], ActiveStatusEffect[]> ACTIVE_EFFECTS = new ActiveEffectsTag();
}
