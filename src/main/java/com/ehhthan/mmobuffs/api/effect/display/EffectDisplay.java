package com.ehhthan.mmobuffs.api.effect.display;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EffectDisplay {
    private final String icon;
    private final String text;

    public EffectDisplay(@NotNull ConfigurationSection section) {
        this.icon = section.getString("icon", "");
        this.text = section.getString("text","<icon> <duration>");
    }

    public String getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }

    // TODO: 10/20/2021 add value ph
    public Component build(@NotNull ActiveStatusEffect effect) {
        if (!effect.isDisplayable())
            return Component.empty();

        List<Template> templates = new ArrayList<>();
        templates.add(Template.of("icon", StringEscapeUtils.unescapeJava(icon)));
        templates.add(Template.of("duration", effect.getDurationDisplay().display()));

        Component parsed = MiniMessage.get().parse(StringEscapeUtils.unescapeJava(text), templates);

        FileConfiguration config = MMOBuffs.getInst().getConfig();
        if (config.getBoolean("resource-pack.enabled")) {
            @Subst("mmobuffs:default") String font = config.getString("resource-pack.font", "mmobuffs:default");
            parsed = parsed.style(parsed.style().font(Key.key(font)));
        }

        return parsed;
    }
}
