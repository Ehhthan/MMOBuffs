package com.ehhthan.mmobuffs.comp.stat.type;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.data.PlayerData;
import com.archyx.aureliumskills.modifier.StatModifier;
import com.archyx.aureliumskills.stats.Stat;
import com.ehhthan.mmobuffs.api.EffectHolder;
import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import com.ehhthan.mmobuffs.api.stat.StatKey;
import com.ehhthan.mmobuffs.api.stat.StatValue;
import com.ehhthan.mmobuffs.comp.stat.StatHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AureliumSkillsStatHandler implements StatHandler<PlayerData> {
    private static final String NAMESPACE = "aureliumskills";

    @NotNull
    @Override
    public String namespace() {
        return NAMESPACE;
    }

    @Nullable
    @Override
    public PlayerData adapt(@NotNull EffectHolder holder) {
        return AureliumAPI.getPlugin().getPlayerManager().getPlayerData(holder.getPlayer());
    }

    @Override
    public void add(@NotNull EffectHolder holder, @NotNull ActiveStatusEffect effect, @NotNull StatKey key, @NotNull StatValue value) {
        PlayerData adapted = adapt(holder);
        if (adapted != null) {
            double modifierValue = switch (effect.getStatusEffect().getStackType()) {
                case NORMAL, CASCADING -> value.getValue() * effect.getStacks();
                default -> value.getValue();
            };

            Stat stat = AureliumAPI.getPlugin().getStatRegistry().getStat(key.getStat());
            if (stat != null)
                adapted.addStatModifier(new StatModifier(key.toString(), stat, modifierValue));
        }
    }

    @Override
    public void remove(@NotNull EffectHolder holder, @NotNull StatKey key) {
        PlayerData adapted = adapt(holder);
        if (adapted != null) {
            adapted.removeStatModifier(key.toString());
        }
    }

    @NotNull
    @Override
    public String getValue(@NotNull EffectHolder holder, @NotNull StatKey key) {
        PlayerData adapted = adapt(holder);
        if (adapted != null) {
            return String.valueOf(adapted.getStatModifier(key.toString()).getValue());
        }
        return "0";
    }
}
