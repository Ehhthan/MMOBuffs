package com.ehhthan.mmobuffs.api.effect.duration;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;

public class EffectDuration {
    protected final int seconds;
    protected final Instant creation;

    public EffectDuration(int seconds) {
        this.seconds = seconds;
        this.creation = Instant.now();
    }

    public int getSeconds() {
        return seconds;
    }

    public Instant getCreation() {
        return creation;
    }

    public int getSecondsLeft() {
        return (int) Math.max(seconds - Duration.between(creation, Instant.now()).getSeconds(), 0);
    }

    public boolean isExpired() {
        return getSecondsLeft() == 0;
    }

    public EffectDuration merge(@NotNull EffectDuration duration) {
        return new EffectDuration(getSecondsLeft() + duration.getSecondsLeft());
    }

    // TODO: 10/20/2021 make this configurable
    @Override
    public String toString() {
        Duration left = Duration.ofSeconds(getSecondsLeft());
        return String.format("%sd %sh %sm %ss",
            left.toDaysPart(),
            left.toHoursPart(),
            left.toMinutesPart(),
            left.toSecondsPart());
    }
}
