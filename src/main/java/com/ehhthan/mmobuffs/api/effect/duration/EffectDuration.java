package com.ehhthan.mmobuffs.api.effect.duration;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.effect.display.Displayable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Template;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;

public class EffectDuration {
    public Component getDisplay() {
        return MMOBuffs.getInst().getLanguageManager().getMessage("permanent-display", false);
    }
    // TODO: 10/20/2021 make this configurable
    public static String formatSeconds(int seconds) {
        Duration left = Duration.ofSeconds(seconds);
        return String.format("%sd %sh %sm %ss",
            left.toDaysPart(),
            left.toHoursPart(),
            left.toMinutesPart(),
            left.toSecondsPart());
    }
}
