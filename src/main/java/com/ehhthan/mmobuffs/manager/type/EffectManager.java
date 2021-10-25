package com.ehhthan.mmobuffs.manager.type;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.effect.StatusEffect;
import com.ehhthan.mmobuffs.file.ConfigFile;
import com.ehhthan.mmobuffs.manager.KeyedManager;
import com.ehhthan.mmobuffs.manager.Reloadable;
import io.lumine.mythic.lib.config.Scope;
import io.lumine.mythic.utils.config.properties.Property;
import io.lumine.mythic.utils.plugin.ReloadableModule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class EffectManager extends KeyedManager<StatusEffect> implements Reloadable {
    public EffectManager() {
        reload();
    }

    @Override
    public void reload() {
        clear();
        ConfigFile config = new ConfigFile("effects");
        for (String key : config.getConfig().getKeys(false))
            try {
                register(new StatusEffect(config.getConfig().getConfigurationSection(key)));
            } catch (IllegalArgumentException e) {
                error(key, e);
            }
    }
}
