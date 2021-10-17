package com.ehhthan.mmobuffs.api.effect;

import com.ehhthan.mmobuffs.MMOBuffs;
import io.lumine.mythic.lib.config.Scope;
import io.lumine.mythic.utils.config.properties.Property;
import io.lumine.mythic.utils.plugin.ReloadableModule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EffectManager extends ReloadableModule<MMOBuffs> {
    private final Set<StatusEffect> effects = new HashSet<>();

    public EffectManager(MMOBuffs plugin) {
        super(plugin, false);
    }

    @Override
    public void load(MMOBuffs mmoBuffs) {
        Property.NodeList()
    }

    @Override
    public void unload() {

    }
}
