package com.ehhthan.mmobuffs.listener;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.EffectHolder;
import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Collection;

public class WorldListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            EffectHolder holder = EffectHolder.get(player);
            Collection<ActiveStatusEffect> effects = holder.getEffects(true);

            if (!MMOBuffs.getInst().getConfig().getBoolean("on-death.remove-timed-effects", true))
                effects.removeIf(e -> !e.isPermanent());
            if (!MMOBuffs.getInst().getConfig().getBoolean("on-death.remove-permanent-effects", false))
                effects.removeIf(ActiveStatusEffect::isPermanent);

            effects.forEach(e -> holder.removeEffect(e.getStatusEffect().getKey()));
        }
    }
}
