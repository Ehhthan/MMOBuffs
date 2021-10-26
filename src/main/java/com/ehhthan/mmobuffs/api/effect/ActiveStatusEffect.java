package com.ehhthan.mmobuffs.api.effect;

import com.ehhthan.mmobuffs.api.effect.duration.EffectDuration;
import com.ehhthan.mmobuffs.api.modifier.Modifier;
import org.jetbrains.annotations.NotNull;

public class ActiveStatusEffect {
    private final StatusEffect effect;
    private final int duration;
    private final int stacks;
    private final boolean showDisplay;

    public ActiveStatusEffect(@NotNull StatusEffect effect) {
        this(effect, -1,  1, true);
    }

    public ActiveStatusEffect(@NotNull StatusEffect effect, int duration) {
        this(effect, duration, 1, true);
    }

    public ActiveStatusEffect(@NotNull StatusEffect effect, int duration, int stacks, boolean showDisplay) {
        this.effect = effect;
        this.duration = duration;
        this.stacks = stacks;
        this.showDisplay = showDisplay;
    }

    public StatusEffect getEffect() {
        return effect;
    }

    public int getDuration() {
        return duration;
    }

    public int getStacks() {
        return stacks;
    }

    public boolean isShowDisplay() {
        return showDisplay;
    }
}
