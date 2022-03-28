package com.ehhthan.mmobuffs.manager.type;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.manager.ConfigFile;
import com.ehhthan.mmobuffs.manager.Reloadable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class LanguageManager implements Reloadable {
    private ConfigFile language;

    private boolean hasWarned = false;

    public LanguageManager() {
        reload();
    }

    public void reload() {
        this.language = new ConfigFile("/language", "language");
    }

    public String getString(@NotNull String path) {
        String found = language.getConfig().getString(path);

        if (found != null && (found.isEmpty() || found.equals("[]")))
            return "";

        return found == null ? "<MNF:" + path + ">" : found;
    }

    @Nullable
    public Component getMessage(@NotNull String path) {
        return getMessage(path, true, null);
    }

    @Nullable
    public Component getMessage(@NotNull String path, boolean hasPrefix) {
        return getMessage(path, hasPrefix, null);
    }

    @Nullable
    public Component getMessage(@NotNull String path, boolean hasPrefix, @Nullable TagResolver resolver) {
        String prefix = (hasPrefix) ? language.getConfig().getString("prefix", "") : "";
        String found = language.getConfig().getString(path);

        if (found != null && (found.isEmpty() || found.equals("[]")))
            return null;

        String input;
        if (found == null) {
            if (!hasWarned) {
                Logger logger = MMOBuffs.getInst().getLogger();
                logger.log(Level.WARNING, "Message Missing: " + path);
                logger.log(Level.WARNING, "You should either add this field yourself to your language.yml or refresh it.");
                hasWarned = true;
            }
            input = "<Message-Missing:" + path + ">";
        } else {
            input = prefix + found;
        }

        if (resolver != null)
            return MiniMessage.miniMessage().deserialize(input, resolver);
        else
            return MiniMessage.miniMessage().deserialize(input);
    }
}
