package com.ehhthan.mmobuffs.comp.placeholderapi;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.EffectHolder;
import com.ehhthan.mmobuffs.api.stat.StatKey;
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
        // Check if the effect holder is loaded and contains the player.
        // Fixes NightFury's issue:
        // java.lang.NullPointerException: Cannot invoke ... because "holder" is null
        if (player != null && player.isOnline() && EffectHolder.has(player.getPlayer())) {
            EffectHolder holder = EffectHolder.get(player.getPlayer());

            String[] split = params.split("_", 2);

            if (split.length == 2) {
                String option = split[0].toLowerCase(Locale.ROOT);
                NamespacedKey key = NamespacedKey.fromString(split[1].toLowerCase(Locale.ROOT), plugin);

                switch (option) {
                    case "name" -> {
                        if (MMOBuffs.getInst().getEffectManager().has(key))
                            return PlainTextComponentSerializer.plainText().serialize(MMOBuffs.getInst().getEffectManager().get(key).getName());
                        else
                            return "";
                    }
                    case "has" -> {
                        return String.valueOf(holder.hasEffect(key));
                    }

                    case "duration" -> {
                        if (holder.hasEffect(key))
                            return PlainTextComponentSerializer.plainText().serialize(holder.getEffect(key).getDurationDisplay().display());
                        else
                            return "0";
                    }

                    case "seconds" -> {
                        if (holder.hasEffect(key))
                            return holder.getEffect(key).getDuration() + "";
                        else
                            return "0";
                    }

                    case "stacks" -> {
                        if (holder.hasEffect(key))
                            return String.valueOf(holder.getEffect(key).getStacks());
                        else
                            return "0";
                    }

                    case "maxstacks" -> {
                        if (holder.hasEffect(key))
                            return String.valueOf(holder.getEffect(key).getStatusEffect().getMaxStacks());
                        else
                            return "0";
                    }

                    default -> {
                        String[] optionSplit = split[1].split("_", 2);
                        String[] statSplit = optionSplit[0].split(":", 2);
                        if (optionSplit.length == 2) {
                            key = NamespacedKey.fromString(optionSplit[1].toLowerCase(Locale.ROOT), plugin);
                            if (key != null && holder.hasEffect(key)) {
                                StatKey statKey;
                                if (statSplit.length == 1)
                                    statKey = new StatKey(plugin.getEffectManager().get(key), statSplit[0]);
                                else if (statSplit.length == 2)
                                    statKey = new StatKey(plugin.getEffectManager().get(key), statSplit[1], statSplit[0]);
                                else
                                    return "0";
                                switch (option) {
                                    case "value" -> {
                                        return plugin.getStatManager().getValue(holder, statKey);
                                    }
                                    case "basevalue" -> {
                                        return holder.getEffect(key).getStatusEffect().getStats().get(statKey).toString();
                                    }
                                }
                            } else
                                return "0";
                        }
                    }
                }
            }
        }
        return null;
    }
}
