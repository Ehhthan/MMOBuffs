package com.ehhthan.mmobuffs.api.effect;

import com.ehhthan.mmobuffs.api.StackType;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Locale;

public class StatusEffect {
    private final String name;
    private final ItemStat stat;

    private final int maxStacks;
    private final StackType stackType;

    private final EffectDisplay display;

    public StatusEffect(ConfigurationSection section) {
        this.name = section.getString("name");
        this.stat = MMOItems.plugin.getStats().get(section.getString("stat"));

        this.maxStacks = section.getInt("max-stacks", 1);
        this.stackType = StackType.valueOf(section.getString("stack-type", "NORMAL").toUpperCase(Locale.ROOT));

        this.display = (section.isConfigurationSection("display"))
            ? new EffectDisplay(section.getConfigurationSection("display"))
            : null;
    }
}
