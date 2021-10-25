package com.ehhthan.mmobuffs.api.effect.display;

import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class EffectDisplay {
    private final String icon;
    private final String text;

    public EffectDisplay(@NotNull ConfigurationSection section) {
        this.icon = section.getString("icon", "");
        this.text = section.getString("text","<icon> <time>");
    }

    public String getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }

    // TODO: 10/20/2021 add value ph
    public Component build(@NotNull ActiveStatusEffect effect) {
        return MiniMessage.get().parse(StringEscapeUtils.unescapeJava(text), "time", effect.getDuration().toString(), "icon", icon);
    }
}
