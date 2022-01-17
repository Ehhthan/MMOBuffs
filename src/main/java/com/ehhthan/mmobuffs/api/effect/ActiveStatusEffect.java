package com.ehhthan.mmobuffs.api.effect;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.effect.display.duration.DurationDisplay;
import com.ehhthan.mmobuffs.api.effect.display.duration.TimedDisplay;
import com.ehhthan.mmobuffs.api.effect.stack.StackType;
import com.ehhthan.mmobuffs.api.modifier.Modifier;
import com.google.common.base.Preconditions;
import net.kyori.adventure.text.minimessage.Template;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ActiveStatusEffect implements TemplateHolder, Comparable<ActiveStatusEffect> {
    private final StatusEffect statusEffect;
    private final int startDuration;
    private final int startStacks;

    private int duration;
    private int stacks;
    private final boolean permanent;

    private final DurationDisplay durationDisplay;
    private boolean active = true;

    private ActiveStatusEffect(ActiveEffectBuilder builder) {
        this.statusEffect = builder.effect;
        this.startDuration = builder.startDuration;
        this.startStacks = builder.startStacks;

        this.duration = builder.duration;
        this.stacks = builder.stacks;
        this.permanent = builder.permanent;

        this.durationDisplay = (permanent) ? DurationDisplay.PERMANENT : new TimedDisplay(this);
    }

    public StatusEffect getStatusEffect() {
        return statusEffect;
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

    public DurationDisplay getDurationDisplay() {
        return durationDisplay;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public boolean isActive() {
        return active;
    }

    public double getStatValue(String key) {
        if (MMOBuffs.getInst().hasStatHandler())
            return MMOBuffs.getInst().getStatHandler().getValue(this, key);
        else
            return 0;
    }

    public boolean tick() {
        if (!permanent && active) {
            this.duration--;
            if (duration <= 0) {
                StackType stackType = statusEffect.getStackType();

                switch (stackType) {
                    case NORMAL, ATTACK, HURT, COMBAT -> {
                        this.stacks = 0;
                        this.active = false;
                    }
                    case CASCADING, TIMESTACK -> {
                        this.stacks--;
                        if (this.stacks <= 0) {
                            this.active = false;
                        } else {
                            this.duration = startDuration;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void tickStackEvent(StackType type) {
        if (active && type == statusEffect.getStackType()) {
            switch (type) {
                case ATTACK, HURT, COMBAT -> {
                    this.stacks--;
                    if (stacks <= 0)
                        this.active = false;
                }
            }
        }
    }

    public void setDuration(int duration) {
        this.duration = Math.max(0, duration);
    }

    public void setStacks(int stacks) {
        this.stacks = Math.max(0, Math.min(stacks, statusEffect.getMaxStacks()));
    }

    @Override
    public List<Template> getTemplates() {
        List<Template> templates = new ArrayList<>();
        templates.add(Template.of("seconds", getDuration() + ""));
        templates.add(Template.of("duration", getDurationDisplay().display()));
        templates.add(Template.of("stacks", getStacks() + ""));
        templates.add(Template.of("start-duration", getStartDuration() + ""));
        templates.add(Template.of("start-stacks", getStartStacks() + ""));

        for (String key : getStatusEffect().getStats().keySet()) {
            templates.add(Template.of("stat:" + key, getStatValue(key) + ""));
        }

        templates.addAll(getStatusEffect().getTemplates());

        return templates;
    }

    public ActiveStatusEffect merge(Modifier modifier, ActiveStatusEffect latest) {
        Preconditions.checkArgument(statusEffect.getKey() == latest.statusEffect.getKey(),
            "Effects of two different types cannot be merged: %s + %s", statusEffect.getKey(), latest.statusEffect.getKey());

        switch (modifier) {
            case SET -> {
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
    public int compareTo(@NotNull ActiveStatusEffect o) {
        return Integer.compare(duration, o.duration);
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
        if (permanent != effect1.permanent) return false;
        if (active != effect1.active) return false;
        return statusEffect.equals(effect1.statusEffect);
    }

    @Override
    public int hashCode() {
        int result = statusEffect.hashCode();
        result = 31 * result + startDuration;
        result = 31 * result + startStacks;
        result = 31 * result + duration;
        result = 31 * result + stacks;
        result = 31 * result + (permanent ? 1 : 0);
        result = 31 * result + (active ? 1 : 0);
        return result;
    }

    public static ActiveEffectBuilder builder(StatusEffect statusEffect) {
        return new ActiveEffectBuilder(statusEffect);
    }


    public static class ActiveEffectBuilder {
        private final StatusEffect effect;

        private int startDuration = 0;
        private int startStacks = 0;

        private int duration = -1;
        private int stacks = -1;
        private boolean permanent = false;

        ActiveEffectBuilder(StatusEffect effect) {
            this.effect = effect;
        }

        ActiveEffectBuilder(ActiveStatusEffect activeEffect) {
            this.effect = activeEffect.statusEffect;
            this.startDuration = activeEffect.startDuration;
            this.startStacks = activeEffect.startStacks;

            this.duration = activeEffect.duration;
            this.stacks = activeEffect.stacks;
            this.permanent = activeEffect.permanent;
        }

        public ActiveEffectBuilder startDuration(int startDuration) {
            this.startDuration = startDuration;
            return this;
        }

        public ActiveEffectBuilder startStacks(int startStacks) {
            this.startStacks = Math.min(effect.getMaxStacks(), startStacks);
            return this;
        }


        public ActiveEffectBuilder duration(int duration) {
            this.duration = Math.max(0, duration);
            return this;
        }

        public ActiveEffectBuilder stacks(int stacks) {
            this.stacks = Math.max(0, Math.min(stacks, effect.getMaxStacks()));
            return this;
        }

        public ActiveEffectBuilder permanent(boolean permanent) {
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