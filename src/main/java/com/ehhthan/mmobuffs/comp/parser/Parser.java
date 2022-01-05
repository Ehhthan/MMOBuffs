package com.ehhthan.mmobuffs.comp.parser;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Parser {
    String parse(@NotNull Player player, @NotNull String text);

    boolean containsPlaceholders(@NotNull String text);
}
