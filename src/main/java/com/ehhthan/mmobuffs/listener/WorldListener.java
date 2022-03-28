package com.ehhthan.mmobuffs.listener;

import com.ehhthan.mmobuffs.api.EffectHolder;
import com.ehhthan.mmobuffs.api.effect.option.EffectOption;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player player)
            removeFalseOption(player, EffectOption.KEEP_ON_DEATH);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        removeFalseOption(event.getPlayer(), EffectOption.KEEP_ON_WORLD_CHANGE);
    }

    // Removes the effect if the option is false and activated.
    private void removeFalseOption(Player player, EffectOption option) {
        EffectHolder holder = EffectHolder.get(player);
        for (NamespacedKey key : holder.getEffects(true).stream().filter(e -> !e.getStatusEffect().getOption(option))
            .map(effect -> effect.getStatusEffect().getKey()).toList()) {
            holder.removeEffect(key);
        }
    }
}
