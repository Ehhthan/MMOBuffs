package com.ehhthan.mmobuffs.comp.stat.type;

import com.ehhthan.mmobuffs.api.EffectHolder;
import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import com.ehhthan.mmobuffs.api.stat.StatKey;
import com.ehhthan.mmobuffs.api.stat.StatValue;
import com.ehhthan.mmobuffs.comp.stat.StatHandler;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import io.lumine.mythic.lib.player.modifier.ModifierSource;
import io.lumine.mythic.lib.player.modifier.ModifierType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class MythicLibStatHandler implements StatHandler<MMOPlayerData> {
    private final static String NAMESPACE = "mythiclib";

    @NotNull
    @Override
    public String namespace() {
        return NAMESPACE;
    }

    @Nullable
    @Override
    public MMOPlayerData adapt(@NotNull EffectHolder holder) {
        return MMOPlayerData.get(holder.getPlayer().getUniqueId());
    }

    @Override
    public void add(@NotNull EffectHolder holder, @NotNull ActiveStatusEffect effect, @NotNull StatKey key, @NotNull StatValue value) {
        MMOPlayerData adapted = adapt(holder);
        if (adapted != null) {
            double modifierValue = switch (effect.getStatusEffect().getStackType()) {
                case NORMAL, CASCADING -> value.getValue() * effect.getStacks();
                default -> value.getValue();
            };

            String stat = key.getStat().toUpperCase(Locale.ROOT);
            adapted.getStatMap().getInstance(stat).registerModifier(new StatModifier(key.getUUID(), key.toString(), stat,
                modifierValue, adaptModifier(value.getType()), EquipmentSlot.OTHER, ModifierSource.OTHER));
        }
    }

    @Override
    public void remove(@NotNull EffectHolder holder, @NotNull StatKey key) {
        MMOPlayerData adapted = adapt(holder);
        if (adapted != null) {
            adapted.getStatMap().getInstance(key.getStat().toUpperCase(Locale.ROOT)).removeModifier(key.getUUID());
        }
    }

    @NotNull
    @Override
    public String getValue(@NotNull EffectHolder holder, @NotNull StatKey key) {
        MMOPlayerData adapted = adapt(holder);
        if (adapted != null) {
            StatModifier modifier = adapted.getStatMap().getInstance(key.getStat()).getModifier(key.getUUID());
            if (modifier != null)
                return modifier.toString();
        }
        return "0";
    }

    /**
     * Convert a buffs value type to mythiclib's ModifierType
     * @param type Stat Value Type
     * @return MythicLib's ModifierType
     */
    private ModifierType adaptModifier(StatValue.ValueType type) {
        return switch (type) {
            case FLAT -> ModifierType.FLAT;
            case RELATIVE -> ModifierType.RELATIVE;
        };
    }
}
