package com.ehhthan.mmobuffs.api.effect;

import com.ehhthan.mmobuffs.api.StackType;
import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Locale;

public class EffectDisplay {
    private final Character icon;
    private final String text;

    public EffectDisplay(ConfigurationSection section) {
        this.icon = section.getObject("icon", Character.class);
        this.text = section.getString("text", "<#>");
    }
}
