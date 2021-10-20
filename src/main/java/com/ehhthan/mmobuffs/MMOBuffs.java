package com.ehhthan.mmobuffs;

import com.ehhthan.mmobuffs.manager.EffectManager;
import io.lumine.mythic.utils.plugin.LuminePlugin;

public final class MMOBuffs extends LuminePlugin {
    private static MMOBuffs plugin;

    private EffectManager effectManager;

    public static MMOBuffs getInst() {
        return plugin;
    }

    @Override
    public void load() {
        plugin = this;
    }

    @Override
    public void enable() {

    }
}
