package com.ehhthan.mmobuffs.api.effect;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.effect.display.EffectDisplay;
import com.ehhthan.mmobuffs.api.effect.stack.StackType;
import com.ehhthan.mmobuffs.api.effect.stat.StatData;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class StatusEffect implements Keyed {
    private final NamespacedKey key;
    private final StatData statData;

    private final int maxStacks;
    private final StackType stackType;

    private final EffectDisplay display;

    public StatusEffect(@NotNull ConfigurationSection section) {
        this.key = NamespacedKey.fromString(section.getName().toLowerCase(Locale.ROOT), MMOBuffs.getInst());

        this.statData = (section.isConfigurationSection("stats"))
            ? new StatData(section.getConfigurationSection("stats"))
            : null;

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

    public boolean hasStatData() {
        return statData != null;
    }

    public @Nullable StatData getStatData() {
        return statData;
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
}
