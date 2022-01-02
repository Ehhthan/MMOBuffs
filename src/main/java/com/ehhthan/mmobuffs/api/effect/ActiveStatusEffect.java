package com.ehhthan.mmobuffs.api.effect;

import com.ehhthan.mmobuffs.api.effect.display.duration.DurationDisplay;
import com.ehhthan.mmobuffs.api.effect.display.duration.TimedDisplay;
import com.ehhthan.mmobuffs.api.effect.stack.StackType;
import com.ehhthan.mmobuffs.api.modifier.Modifier;
import com.google.common.base.Preconditions;

public class ActiveStatusEffect {
    private final StatusEffect effect;
    private final int startDuration;
    private final int startStacks;

    private int duration;
    private int stacks;
    private final boolean displayable;
    private final boolean permanent;

    private final DurationDisplay durationDisplay;
    private boolean active = true;

    private ActiveStatusEffect(EffectBuilder builder) {
        this.effect = builder.effect;
        this.startDuration = builder.startDuration;
        this.startStacks = builder.startStacks;

        this.duration = builder.duration;
        this.stacks = builder.stacks;
        this.displayable = builder.displayable;
        this.permanent = builder.permanent;

        this.durationDisplay = (permanent) ? DurationDisplay.PERMANENT : new TimedDisplay(this);
    }

    public StatusEffect getEffect() {
        return effect;
    }

    public int getStartDuration() {
        return startDuration;
    }

    public int getStartStacks() {
        return startStacks;
    }

    public int getDuration() {
        return duration;
    }

    public int getStacks() {
        return stacks;
    }

    public boolean isDisplayable() {
        return displayable;
    }

    public DurationDisplay getDurationDisplay() {
        return durationDisplay;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public boolean isActive() {
        return active;
    }

    public void tick() {
        if (!permanent && active) {
            this.duration--;
            if (duration <= 0) {
                StackType stackType = effect.getStackType();

                switch (stackType) {
                    case NORMAL, ATTACK, HURT, COMBAT -> {
                        this.stacks = 0;
                        this.active = false;
                    }
                    case CASCADING, TIMESTACK -> {
                        if (this.stacks == 0) {
                            this.active = false;
                        } else {
                            this.stacks--;
                            this.duration = startDuration;
                        }
                    }
                }
            }
        }
    }

    public void setDuration(int duration) {
        this.duration = Math.max(0, duration);
    }

    public void setStacks(int stacks) {
        this.stacks = Math.max(0, Math.min(stacks, effect.getMaxStacks()));
    }

    public ActiveStatusEffect merge(Modifier modifier, ActiveStatusEffect latest) {
        Preconditions.checkArgument(effect.getKey() == latest.effect.getKey(),
            "Effects of two different types cannot be merged: %s + %s", effect.getKey(), latest.effect.getKey());

        switch (modifier) {
            case REPLACE -> {
                return latest;
            }

            case KEEP -> {
                return this;
            }

            case REFRESH -> {
                if (this.duration < latest.duration)
                    this.duration = latest.duration;

                return this;
            }

            case ADD -> {
                this.duration = Math.max(0, this.duration + latest.duration);
                return this;
            }

            case SUBTRACT -> {
                this.duration = Math.max(0, this.duration - latest.duration);
                return this;
            }

            default -> throw new IllegalStateException("Unexpected value: " + modifier);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActiveStatusEffect effect1 = (ActiveStatusEffect) o;

        if (startDuration != effect1.startDuration) return false;
        if (startStacks != effect1.startStacks) return false;
        if (duration != effect1.duration) return false;
        if (stacks != effect1.stacks) return false;
        if (displayable != effect1.displayable) return false;
        if (permanent != effect1.permanent) return false;
        if (active != effect1.active) return false;
        return effect.equals(effect1.effect);
    }

    @Override
    public int hashCode() {
        int result = effect.hashCode();
        result = 31 * result + startDuration;
        result = 31 * result + startStacks;
        result = 31 * result + duration;
        result = 31 * result + stacks;
        result = 31 * result + (displayable ? 1 : 0);
        result = 31 * result + (permanent ? 1 : 0);
        result = 31 * result + (active ? 1 : 0);
        return result;
    }

    public static EffectBuilder builder(StatusEffect effect) {
        return new EffectBuilder(effect);
    }

    public static class EffectBuilder {
        private final StatusEffect effect;

        private int startDuration = 0;
        private int startStacks = 0;

        private int duration = -1;
        private int stacks = -1;
        private boolean displayable = true;
        private boolean permanent = false;

        EffectBuilder(StatusEffect effect) {
            this.effect = effect;
        }

        EffectBuilder(ActiveStatusEffect activeEffect) {
            this.effect = activeEffect.effect;
            this.startDuration = activeEffect.startDuration;
            this.startStacks = activeEffect.startStacks;

            this.duration = activeEffect.duration;
            this.stacks = activeEffect.stacks;
            this.displayable = activeEffect.displayable;
            this.permanent = activeEffect.permanent;
        }

        public EffectBuilder startDuration(int startDuration) {
            this.startDuration = startDuration;
            return this;
        }

        public EffectBuilder startStacks(int startStacks) {
            this.startStacks = Math.min(effect.getMaxStacks(), startStacks);
            return this;
        }


        public EffectBuilder duration(int duration) {
            this.duration = Math.max(0, duration);
            return this;
        }

        public EffectBuilder stacks(int stacks) {
            this.stacks = Math.max(0, Math.min(stacks, effect.getMaxStacks()));
            return this;
        }

        public EffectBuilder displayable(boolean displayable) {
            this.displayable = displayable;
            return this;
        }

        public EffectBuilder permanent(boolean permanent) {
            this.permanent = permanent;
            return this;
        }

        public ActiveStatusEffect build() {
            if (duration == -1)
                duration = startDuration;
            if (stacks == -1)
                stacks = startStacks;
            return new ActiveStatusEffect(this);
        }
    }
}