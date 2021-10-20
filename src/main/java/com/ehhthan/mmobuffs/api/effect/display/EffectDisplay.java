package com.ehhthan.mmobuffs.api.effect.display;

import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import com.ehhthan.mmobuffs.api.effect.duration.EffectDuration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.template.TemplateResolver;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class EffectDisplay {
    private final String icon;
    private final String text;

    private final TemplateResolver resolver;

    public EffectDisplay(@NotNull ConfigurationSection section) {
        this.icon = section.getString("icon", "");
        this.text = section.getString("text","<icon> <time>");

        this.resolver = TemplateResolver.resolving("icon", icon);
    }

    public String getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }

    // TODO: 10/20/2021 add value ph
    public Component build(@NotNull ActiveStatusEffect effect) {
        TemplateResolver buildResolver = TemplateResolver.combining(resolver, TemplateResolver.resolving("time", effect.getDuration().toString()));

        return MiniMessage.miniMessage().deserialize(text, buildResolver);
    }
}
