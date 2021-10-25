package com.ehhthan.mmobuffs.file;

import com.ehhthan.mmobuffs.MMOBuffs;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigFile {
    private final Plugin plugin;
    private final String path, name;

    private final FileConfiguration config;

    public ConfigFile(String name) {
        this(MMOBuffs.getInst(), "", name);
    }

    public ConfigFile(Plugin plugin, String path, String name) {
        this.plugin = plugin;
        this.path = path;
        this.name = name;

        config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + path, name + ".yml"));
    }

    public ConfigFile(Plugin plugin, String name) {
        this(plugin, "", name);
    }

    public ConfigFile(String path, String name) {
        this(MMOBuffs.getInst(), path, name);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            config.save(new File(plugin.getDataFolder() + path, name + ".yml"));
        } catch (IOException exception) {
            MMOBuffs.getInst().getLogger().log(Level.SEVERE, "Could not save " + name + ".yml: " + exception.getMessage());
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setup() {
        try {
            if (!new File(plugin.getDataFolder() + path).exists())
                new File(plugin.getDataFolder() + path).mkdir();

            if (!new File(plugin.getDataFolder() + path, name + ".yml").exists()) {
                new File(plugin.getDataFolder() + path, name + ".yml").createNewFile();
            }
        } catch (IOException exception) {
            MMOBuffs.getInst().getLogger().log(Level.SEVERE, "Could not generate " + name + ".yml: " + exception.getMessage());
        }
    }

}
