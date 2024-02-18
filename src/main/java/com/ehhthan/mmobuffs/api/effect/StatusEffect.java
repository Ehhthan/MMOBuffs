package com.ehhthan.mmobuffs.api.effect;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.effect.display.EffectDisplay;
import com.ehhthan.mmobuffs.api.effect.option.EffectOption;
import com.ehhthan.mmobuffs.api.effect.stack.StackType;
import com.ehhthan.mmobuffs.api.stat.StatKey;
import com.ehhthan.mmobuffs.api.stat.StatValue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class StatusEffect implements Keyed, Resolver {
    private final NamespacedKey key;
    private final Component name;
    private final Component description;

    private final Map<StatKey, StatValue> stats = new LinkedHashMap<>();
    private final Map<EffectOption, Boolean> options = new HashMap<>();

    private final int maxStacks;
    private final StackType stackType;

    private final EffectDisplay display;

    @SuppressWarnings("ConstantConditions")
    public StatusEffect(@NotNull ConfigurationSection section) {
        this.key = NamespacedKey.fromString(section.getName().toLowerCase(Locale.ROOT), MMOBuffs.getInst());
        this.name = MiniMessage.miniMessage().deserialize(section.getString("display-name", WordUtils.capitalize(key.getKey())));
        this.description = MiniMessage.miniMessage().deserialize(section.getString("description", ""));

        if (section.isConfigurationSection("stats")) {
            ConfigurationSection statSection = section.getConfigurationSection("stats");
            for (String stat : statSection.getKeys(false)) {
                String[] split = stat.split(":", 2);

                StatKey statKey;
                if (split.length == 1)
                    statKey = new StatKey(this, split[0]);
                else if (split.length == 2)
                    statKey = new StatKey(this, split[1], split[0]);
                else
                    continue;

                stats.put(statKey, new StatValue(statSection.getString(stat)));
            }
        }

        if (section.isConfigurationSection("options")) {
            ConfigurationSection optionSection = section.getConfigurationSection("options");
            for (String key : optionSection.getKeys(false)) {
                options.put(EffectOption.fromPath(key), optionSection.getBoolean(key));
            }
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

    public Component getDescription() {
        return description;
    }

    public boolean hasStats() {
        return !stats.isEmpty();
    }

    public Map<StatKey, StatValue> getStats() {
        return stats;
    }

    public boolean getOption(EffectOption option) {
        return options.getOrDefault(option, option.defValue());
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
    public TagResolver getResolver() {
        TagResolver.Builder resolver = TagResolver.builder()
            .resolver(Placeholder.parsed("max-stacks", getMaxStacks() + ""))
            .resolver(Placeholder.component("name", name))
            .resolver(Placeholder.component("description", description))
            .resolver(Placeholder.parsed("stack-type", WordUtils.capitalize(stackType.name().toLowerCase(Locale.ROOT))));

        return resolver.build();
    }
}
