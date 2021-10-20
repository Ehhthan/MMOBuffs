package com.ehhthan.mmobuffs.api.effect;

import com.ehhthan.mmobuffs.api.StackType;
import com.ehhthan.mmobuffs.api.effect.display.EffectDisplay;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.stat.type.DoubleStat;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class StatusEffect {
    private final String name;
    private final DoubleStat stat;

    private final int maxStacks;
    private final StackType stackType;

    private final EffectDisplay display;

    public StatusEffect(@NotNull ConfigurationSection section) {
        this.name = section.getString("name");

        ItemStat itemStat = MMOItems.plugin.getStats().get(section.getString("stat"));
        if (itemStat instanceof DoubleStat)
            this.stat = (DoubleStat) itemStat;
        else
            throw new IllegalArgumentException("Stat '" + itemStat.getId() + "' is not a DoubleStat.");

        this.maxStacks = section.getInt("max-stacks", 1);
        this.stackType = StackType.valueOf(section.getString("stack-type", "NORMAL").toUpperCase(Locale.ROOT));

        this.display = (section.isConfigurationSection("display"))
            ? new EffectDisplay(section.getConfigurationSection("display"))
            : null;
    }

    public String getName() {
        return name;
    }

    public ItemStat getStat() {
        return stat;
    }

    public int getMaxStacks() {
        return maxStacks;
    }

    public StackType getStackType() {
        return stackType;
    }

    public @Nullable EffectDisplay getDisplay() {
        return display;
    }
}
