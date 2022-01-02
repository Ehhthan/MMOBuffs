package com.ehhthan.mmobuffs.manager.type;

import com.ehhthan.mmobuffs.manager.ConfigFile;
import com.ehhthan.mmobuffs.manager.Reloadable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LanguageManager implements Reloadable {
    private ConfigFile language;

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
        return getMessage(path, true);
    }

    @Nullable
    public Component getMessage(@NotNull String path, boolean hasPrefix, Template... templates) {
        String prefix = (hasPrefix) ? language.getConfig().getString("prefix", "") : "";
        String found = language.getConfig().getString(path);

        if (found != null && (found.isEmpty() || found.equals("[]")))
            return null;

        String input = found == null ? "<MNF:" + path + ">" : prefix + found;

        if (templates != null && templates.length > 0)
            return MiniMessage.get().parse(input, templates);
        else
            return MiniMessage.get().parse(input);
    }
}
