package com.ehhthan.mmobuffs;

import com.ehhthan.mmobuffs.command.MMOBuffsCommand;
import com.ehhthan.mmobuffs.manager.type.ConfigManager;
import com.ehhthan.mmobuffs.manager.type.EffectManager;
import com.ehhthan.mmobuffs.manager.type.LanguageManager;
import io.lumine.mythic.utils.plugin.LuminePlugin;

public final class MMOBuffs extends LuminePlugin {
    private static MMOBuffs plugin;


    private ConfigManager configManager;
    private LanguageManager languageManager;
    private EffectManager effectManager;

    @Override
    public void enable() {
        plugin = this;
        saveDefaultConfig();

        final int configVersion = getConfig().contains("config-version", true) ? getConfig().getInt("config-version") : -1;
        final int defConfigVersion = getConfig().getDefaults().getInt("config-version", -1);
        if (configVersion != defConfigVersion) {
            getLogger().warning("You may be using an outdated config.yml!");
            getLogger().warning("(Your config version: '" + configVersion + "' | Expected config version: '"
                + defConfigVersion + "')");
        }

        this.registerCommand("mmobuffs", new MMOBuffsCommand());

        this.configManager = new ConfigManager();
        this.languageManager = new LanguageManager();
        this.effectManager = new EffectManager();
    }

    public void reload() {
        reloadConfig();

        languageManager.reload();
        effectManager.reload();
    }

    public static MMOBuffs getInst() {
        return plugin;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public EffectManager getEffectManager() {
        return effectManager;
    }
}
