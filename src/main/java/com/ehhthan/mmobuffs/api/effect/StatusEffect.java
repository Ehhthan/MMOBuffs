package com.ehhthan.mmobuffs.api.effect;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.effect.display.EffectDisplay;
import com.ehhthan.mmobuffs.api.effect.stack.StackType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatusEffect implements Keyed, TemplateHolder {
    private final NamespacedKey key;
    private final Component name;

    private final Map<String, Double> stats;

    private final int maxStacks;
    private final StackType stackType;

    private final EffectDisplay display;

    @SuppressWarnings("ConstantConditions")
    public StatusEffect(@NotNull ConfigurationSection section) {
        this.key = NamespacedKey.fromString(section.getName().toLowerCase(Locale.ROOT), MMOBuffs.getInst());
        this.name = MiniMessage.get().parse(section.getString("display-name", WordUtils.capitalize(key.getKey())));

        if (section.isConfigurationSection("stats")) {
            this.stats = new LinkedHashMap<>();

            ConfigurationSection statSection = section.getConfigurationSection("stats");
            for (String key : statSection.getKeys(false)) {
                if (statSection.isSet(key))
                    stats.put(key.toLowerCase(Locale.ROOT), statSection.getDouble(key));
            }
        } else {
            this.stats = null;
        }

        this.maxStacks = section.getInt("max-stacks", 1);
        this.stackType = StackType.valueOf(section.getString("stack-type", "NORMAL").toUpperCase(Locale.ROOT));

        this.display = (section.isConfigurationSection("display"))
            ? new EffectDisplay(section.getConfigurationSection("display"))
            : null;
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    public Component getName() {
        return name;
    }

    public boolean hasStats() {
        return stats != null && !stats.isEmpty();
    }

    public Map<String, Double> getStats() {
        return stats;
    }

    public int getMaxStacks() {
        return maxStacks;
    }

    public StackType getStackType() {
        return stackType;
    }

    public boolean hasDisplay() {
        return display != null;
    }

    public @Nullable EffectDisplay getDisplay() {
        return display;
    }

    @Override
    public Collection<Template> getTemplates() {
        List<Template> templates = new ArrayList<>();

        templates.add(Template.of("max-stacks", getMaxStacks() + ""));
        templates.add(Template.of("name", name));
        templates.add(Template.of("stack-type", WordUtils.capitalize(stackType.name().toLowerCase(Locale.ROOT))));

        return templates;
    }
}
