package com.ehhthan.mmobuffs.manager.type;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.EffectHolder;
import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import com.ehhthan.mmobuffs.api.stat.StatKey;
import com.ehhthan.mmobuffs.api.stat.StatValue;
import com.ehhthan.mmobuffs.comp.stat.StatHandler;
import com.ehhthan.mmobuffs.comp.stat.type.AureliumSkillsStatHandler;
import com.ehhthan.mmobuffs.comp.stat.type.MythicLibStatHandler;
import com.ehhthan.mmobuffs.comp.stat.type.MythicMobsStatHandler;
import org.bukkit.Bukkit;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

public class StatManager {
    private final MMOBuffs plugin;
    private final Map<String, StatHandler<?>> handlers = new LinkedHashMap<>();

    public StatManager(MMOBuffs plugin) {
        this.plugin = plugin;
        if (Bukkit.getPluginManager().getPlugin("MythicLib") != null)
            register(new MythicLibStatHandler());

        if (Bukkit.getPluginManager().getPlugin("AureliumSkills") != null)
            register(new AureliumSkillsStatHandler());

        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null)
            register(new MythicMobsStatHandler());
    }

    public void register(StatHandler<?> handler) {
        handlers.put(handler.namespace(), handler);
        plugin.getLogger().log(Level.INFO, "StatHandler registered successfully: " + handler.namespace());
    }

    public void add(EffectHolder holder, ActiveStatusEffect effect) {
        if (holder != null) {
            for (Map.Entry<StatKey, StatValue> entry : effect.getStatusEffect().getStats().entrySet()) {
                getHandler(entry.getKey()).add(holder, effect, entry.getKey(), entry.getValue());
            }
        }
    }

    public void remove(EffectHolder holder, ActiveStatusEffect effect) {
        if (holder != null) {
            for (StatKey key : effect.getStatusEffect().getStats().keySet()) {
                getHandler(key).remove(holder, key);
            }
        }
    }

    public String getValue(EffectHolder holder, StatKey key) {
        if (holder != null && key != null) {
            return getHandler(key).getValue(holder, key);
        }
        return "0";
    }

    private StatHandler<?> getHandler(StatKey key) {
        return handlers.get((key.getPlugin() != null) ? key.getPlugin() : plugin.getConfig().getString("stat-handler.default", "mythiclib"));
    }
}
