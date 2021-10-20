package com.ehhthan.mmobuffs.manager;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.effect.StatusEffect;
import io.lumine.mythic.lib.config.Scope;
import io.lumine.mythic.utils.config.properties.Property;
import io.lumine.mythic.utils.plugin.ReloadableModule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EffectManager {
    private final Set<StatusEffect> effects = new HashSet<>();

    public EffectManager() {

    }
}
