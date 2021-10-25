package com.ehhthan.mmobuffs.api.effect.duration;

import com.ehhthan.mmobuffs.MMOBuffs;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class PermanentDuration extends EffectDuration {
    private static final PermanentDuration INSTANCE = new PermanentDuration();

    private PermanentDuration() {
        super(-1);
    }

    public static PermanentDuration get() {
        return INSTANCE;
    }

    @Override
    public int getSecondsLeft() {
        return -1;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public EffectDuration merge(@NotNull EffectDuration duration) {
        MMOBuffs.getInst().getLogger().log(Level.WARNING, "PermanentDurations should not be attempted to be merged.");
        return this;
    }

    // TODO: 10/20/2021 make this configurable
    @Override
    public String toString() {
        return "Permanent";
    }
}
