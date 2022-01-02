package com.ehhthan.mmobuffs.api.effect.display.duration;

import com.ehhthan.mmobuffs.MMOBuffs;
import net.kyori.adventure.text.Component;

public interface DurationDisplay {
    DurationDisplay PERMANENT = () -> MMOBuffs.getInst().getLanguageManager().getMessage("duration-display.permanent", false);

    Component display();
}
