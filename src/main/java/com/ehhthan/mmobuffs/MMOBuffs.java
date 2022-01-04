package com.ehhthan.mmobuffs;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import com.ehhthan.mmobuffs.api.EffectHolder;
import com.ehhthan.mmobuffs.api.effect.StatusEffect;
import com.ehhthan.mmobuffs.command.MMOBuffsCommand;
import com.ehhthan.mmobuffs.comp.placeholderapi.MMOBuffsExpansion;
import com.ehhthan.mmobuffs.comp.stat.StatHandler;
import com.ehhthan.mmobuffs.comp.stat.type.MythicLibStatHandler;
import com.ehhthan.mmobuffs.listener.CombatListener;
import com.ehhthan.mmobuffs.listener.WorldListener;
import com.ehhthan.mmobuffs.manager.type.ConfigManager;
import com.ehhthan.mmobuffs.manager.type.EffectManager;
import com.ehhthan.mmobuffs.manager.type.LanguageManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class MMOBuffs extends JavaPlugin {
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private EffectManager effectManager;

    private StatHandler<?> statHandler;

    private static MMOBuffs INSTANCE;

    public static MMOBuffs getInst() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        saveDefaultConfig();

        final int configVersion = getConfig().contains("config-version", true) ? getConfig().getInt("config-version") : -1;
        final int defConfigVersion = getConfig().getDefaults().getInt("config-version", -1);
        if (configVersion != defConfigVersion) {
            getLogger().warning("You may be using an outdated config.yml!");
            getLogger().warning("(Your config version: '" + configVersion + "' | Expected config version: '"
                + defConfigVersion + "')");
        }

        this.configManager = new ConfigManager(this);
        this.languageManager = new LanguageManager();
        this.effectManager = new EffectManager();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new MMOBuffsExpansion().register();
            getLogger().log(Level.INFO, "PlaceholderAPI support detected.");
        }

        if (Bukkit.getPluginManager().getPlugin("MythicLib") != null) {
            this.statHandler = new MythicLibStatHandler();
            getLogger().log(Level.INFO, "MythicLib stat support detected.");
        }

        getServer().getPluginManager().registerEvents(new EffectHolder.PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);
        getServer().getPluginManager().registerEvents(new CombatListener(), this);

        registerCommands();
    }

    private void registerCommands() {
        PaperCommandManager commandManager = new PaperCommandManager(this);

        commandManager.getCommandCompletions().registerAsyncCompletion("effects",
            c -> MMOBuffs.getInst().getEffectManager().keys().stream().map(NamespacedKey::getKey).toList());

        commandManager.getCommandContexts().registerContext(StatusEffect.class, c -> {
            String arg = c.getFirstArg();
            NamespacedKey key = arg != null ? NamespacedKey.fromString(arg, this) : null;

            if (key == null || !effectManager.has(key)) {
                throw new InvalidCommandArgument("Invalid status effect specified.");
            } else {
                c.popFirstArg();
            }

            return effectManager.get(key);
        });

        commandManager.getCommandContexts().registerContext(EffectHolder.class, c -> {
            String arg = c.getFirstArg();
            Player player = arg != null ? Bukkit.getPlayer(arg) : null;

            if (!EffectHolder.has(player)) {
                throw new InvalidCommandArgument("Invalid effect holder specified.");
            } else {
                c.popFirstArg();
            }

            return EffectHolder.get(player);
        });

        commandManager.registerCommand(new MMOBuffsCommand(this, languageManager));
    }

    public void reload() {
        reloadConfig();

        languageManager.reload();
        effectManager.reload();
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

    public StatHandler<?> getStatHandler() {
        return statHandler;
    }
}
