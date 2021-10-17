package com.ehhthan.mmobuffs;

import com.ehhthan.mmobuffs.api.effect.EffectManager;
import io.lumine.mythic.lib.metrics.bStats;
import io.lumine.mythic.utils.plugin.LuminePlugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class MMOBuffs extends LuminePlugin {
    private static MMOBuffs plugin;

    private EffectManager effectManager;

    @Override
    public void load() {
        plugin = this;
    }

    @Override
    public void enable() {
        this.effectManager = new EffectManager(this);

        this.bind(this.effectManager);
    }
}
