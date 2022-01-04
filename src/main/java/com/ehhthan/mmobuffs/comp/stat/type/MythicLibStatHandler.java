package com.ehhthan.mmobuffs.comp.stat.type;

import com.ehhthan.mmobuffs.api.EffectHolder;
import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import com.ehhthan.mmobuffs.comp.stat.StatHandler;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;

import java.util.Locale;
import java.util.Map;

public class MythicLibStatHandler implements StatHandler<MMOPlayerData> {

    @Override
    public void edit(EffectHolder holder, EditType type, ActiveStatusEffect effect) {
        if (holder.getPlayer().isOnline()) {
            for (Map.Entry<String, Double> entry : effect.getStatusEffect().getStats().entrySet()) {
                // Example modifierKey: "<effect-name>:<stat-name>" or "defense-effect:max-health"
                String modifierKey = effect.getStatusEffect().getKey().getKey() + ":" + entry.getKey();

                MMOPlayerData adapted = adapt(holder);
                switch (type) {
                    case ADD -> adapted.getStatMap().getInstance(entry.getKey().toUpperCase(Locale.ROOT))
                        .addModifier(modifierKey, new StatModifier(getValue(effect, entry.getKey())));
                    case REMOVE -> adapted.getStatMap().getInstance(entry.getKey().toUpperCase(Locale.ROOT)).remove(modifierKey);
                }
            }
        }
    }

    @Override
    public double getValue(ActiveStatusEffect effect, String key) {
        Map<String, Double> stats = effect.getStatusEffect().getStats();
        if (stats.containsKey(key)) {
            double value = stats.get(key);
            return switch (effect.getStatusEffect().getStackType()) {
                case NORMAL, CASCADING -> value * effect.getStacks();
                case TIMESTACK, ATTACK, HURT, COMBAT -> value;
            };
        } else {
            return 0;
        }
    }

    @Override
    public MMOPlayerData adapt(EffectHolder holder) {
        return MMOPlayerData.get(holder.getPlayer().getUniqueId());
    }
}
