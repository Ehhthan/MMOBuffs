package com.ehhthan.mmobuffs.api.effect.stat;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StatData {
    private final Map<String, Double> stats = new HashMap<>();

    public StatData(@NotNull ConfigurationSection section) {
        for (String key : section.getKeys(false)) {
            if (section.isDouble(key))
                stats.put(format(key), section.getDouble(key));
        }
    }

    public boolean hasStat(String stat) {
        return stats.containsKey(format(stat));
    }

    public Double getValue(String stat) {
        return stats.get(format(stat));
    }

    private String format(String key) {
        return key.replace("-", "_").replace(" ", "_").toLowerCase(Locale.ROOT);
    }
}
