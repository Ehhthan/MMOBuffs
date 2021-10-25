package com.ehhthan.mmobuffs.api.effect;

import com.ehhthan.mmobuffs.api.effect.stack.StackType;
import com.ehhthan.mmobuffs.api.effect.display.EffectDisplay;
import com.ehhthan.mmobuffs.api.effect.stat.StatData;
import com.ehhthan.mmobuffs.manager.Keyable;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.stat.type.DoubleStat;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class StatusEffect implements Keyable {
    private final String key;
    private final StatData statData;

    private final int maxStacks;
    private final StackType stackType;

    private final EffectDisplay display;

    public StatusEffect(@NotNull ConfigurationSection section) {
        this.key = Keyable.format(section.getName());

        Validate.isTrue(section.isConfigurationSection("stats"));

        this.statData = new StatData(section.getConfigurationSection("stats"));

        this.maxStacks = section.getInt("max-stacks", 1);
        this.stackType = StackType.valueOf(section.getString("stack-type", "NORMAL").toUpperCase(Locale.ROOT));

        this.display = (section.isConfigurationSection("display"))
            ? new EffectDisplay(section.getConfigurationSection("display"))
            : null;
    }

    @Override
    public String getKey() {
        return key;
    }

    public StatData getStatData() {
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
