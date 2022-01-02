package com.ehhthan.mmobuffs.manager.type;

import com.ehhthan.mmobuffs.api.effect.StatusEffect;
import com.ehhthan.mmobuffs.manager.ConfigFile;
import com.ehhthan.mmobuffs.manager.KeyedManager;
import com.ehhthan.mmobuffs.manager.Reloadable;

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
