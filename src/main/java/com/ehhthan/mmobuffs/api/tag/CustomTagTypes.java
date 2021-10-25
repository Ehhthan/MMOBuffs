package com.ehhthan.mmobuffs.api.tag;

import com.ehhthan.mmobuffs.api.effect.duration.EffectDuration;
import com.ehhthan.mmobuffs.api.tag.custom.BooleanTag;
import com.ehhthan.mmobuffs.api.tag.custom.EffectDurationTag;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public interface CustomTagTypes {
    PersistentDataType<Byte, Boolean> BOOLEAN = new BooleanTag();
    PersistentDataType<PersistentDataContainer, EffectDuration> EFFECT_DURATION = new EffectDurationTag();
}
