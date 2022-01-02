package com.ehhthan.mmobuffs.comp.placeholderapi;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.EffectHolder;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class MMOBuffsExpansion extends PlaceholderExpansion {
    private final MMOBuffs plugin;

    public MMOBuffsExpansion() {
        this.plugin = MMOBuffs.getInst();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mmobuffs";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player != null && player.isOnline()) {
            EffectHolder holder = EffectHolder.get(player.getPlayer());
            if (params.startsWith("has_")) {
                return String.valueOf(holder.hasEffect(NamespacedKey.fromString(params.replace("has_", "").toLowerCase(Locale.ROOT), plugin)));
            }
            else if (params.startsWith("duration_")) {
                NamespacedKey key = NamespacedKey.fromString(params.replace("duration_", "").toLowerCase(Locale.ROOT), plugin);
                if (holder.hasEffect(key))
                    return PlainTextComponentSerializer.plainText().serialize(holder.getEffect(key).getDurationDisplay().display());
                else
                    return "0";
            }
        }
        return null;
    }
}
