package com.ehhthan.mmobuffs.api.effect;

import com.ehhthan.mmobuffs.api.effect.duration.EffectDuration;
import io.lumine.mythic.lib.MythicLib;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.player.PlayerData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActiveStatusEffect {
    private final Player player;
    private final StatusEffect effect;
    private final EffectDuration duration;

    public ActiveStatusEffect(@NotNull Player player, @NotNull StatusEffect effect, @NotNull EffectDuration duration) {
        this.player = player;
        this.effect = effect;
        this.duration = duration;
    }

    public Player getPlayer() {
        return player;
    }

    public StatusEffect getEffect() {
        return effect;
    }

    public EffectDuration getDuration() {
        return duration;
    }
}
