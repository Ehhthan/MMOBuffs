package com.ehhthan.mmobuffs.api.effect.display;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

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

    public Component build(@NotNull Player player, @NotNull ActiveStatusEffect effect) {
        TagResolver resolver = TagResolver.builder().resolver(effect.getResolver()).resolver(Placeholder.parsed("icon", StringEscapeUtils.unescapeJava(icon))).build();

        Component parsed = MiniMessage.miniMessage().deserialize((MMOBuffs.getInst().getParserManager().parse(player, StringEscapeUtils.unescapeJava(text))), resolver);

        FileConfiguration config = MMOBuffs.getInst().getConfig();
        if (config.getBoolean("resource-pack.enabled")) {
            @Subst("mmobuffs:default") String font = config.getString("resource-pack.font", "mmobuffs:default");
            parsed = parsed.style(parsed.style().font(Key.key(font)));
        }

        return parsed;
    }
}
