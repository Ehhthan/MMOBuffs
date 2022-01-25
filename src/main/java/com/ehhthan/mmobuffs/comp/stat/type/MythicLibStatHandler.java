package com.ehhthan.mmobuffs.comp.stat.type;

import com.ehhthan.mmobuffs.api.EffectHolder;
import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import com.ehhthan.mmobuffs.comp.stat.StatHandler;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import io.lumine.mythic.lib.player.modifier.ModifierType;

import java.util.Locale;
import java.util.Map;

public class MythicLibStatHandler implements StatHandler<MMOPlayerData> {
    @Override
    public void edit(EffectHolder holder, EditType type, ActiveStatusEffect effect) {
        if (holder.getPlayer().isOnline()) {
            for (Map.Entry<String, String> entry : effect.getStatusEffect().getStats().entrySet()) {
                // Example modifierKey: "<effect-name>:<stat-name>" or "defense-effect:max-health"
                String modifierKey = effect.getStatusEffect().getKey().getKey() + ":" + entry.getKey();

                String stat = entry.getKey().toUpperCase(Locale.ROOT);
                String value = entry.getValue();

                MMOPlayerData adapted = adapt(holder);
                switch (type) {
                    case ADD -> {
                        ModifierType modifierType = value.toCharArray()[value.length() - 1] == '%' ? ModifierType.RELATIVE : ModifierType.FLAT;
                        double numberValue = Double.parseDouble(modifierType == ModifierType.RELATIVE ? value.substring(0, value.length() - 1) : value);

                        numberValue = switch (effect.getStatusEffect().getStackType()) {
                            case NORMAL, CASCADING -> numberValue * effect.getStacks();
                            default -> numberValue;
                        };

                        adapted.getStatMap().getInstance(stat).addModifier(new StatModifier(modifierKey, stat, numberValue, modifierType));
                    }
                    case REMOVE -> adapted.getStatMap().getInstance(stat).remove(modifierKey);
                }
            }
        }
    }

    @Override
    public String getValue(EffectHolder holder, String key) {
        String[] split = key.split(":", 2);
        if (split.length != 2)
            return "0";

        StatModifier modifier = adapt(holder).getStatMap().getInstance(split[1].toUpperCase(Locale.ROOT)).getModifier(key);
        if (modifier != null)
            return modifier.toString();
        else
            return "0";
    }

    @Override
    public MMOPlayerData adapt(EffectHolder holder) {
        return MMOPlayerData.get(holder.getPlayer().getUniqueId());
    }
}
