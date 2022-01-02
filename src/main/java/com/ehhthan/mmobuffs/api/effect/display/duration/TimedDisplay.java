package com.ehhthan.mmobuffs.api.effect.display.duration;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class TimedDisplay implements DurationDisplay {
    private static final Function<Long, Component> DAYS_FORMAT = (value) -> {
        Component component = Component.empty();

        if (value > 0) {
            component = MiniMessage.get().parse(MMOBuffs.getInst().getLanguageManager().getString("duration-display.days"), "value", value + "");
        }

        return component;
    };

    private static final Function<Integer, Component> HOURS_FORMAT = (value) -> {
        Component component = Component.empty();

        if (value > 0) {
            component = MiniMessage.get().parse(MMOBuffs.getInst().getLanguageManager().getString("duration-display.hours"), "value", value + "");
        }

        return component;
    };

    private static final Function<Integer, Component> MINUTES_FORMAT = (value) -> {
        Component component = Component.empty();

        if (value > 0) {
            component = MiniMessage.get().parse(MMOBuffs.getInst().getLanguageManager().getString("duration-display.minutes"), "value", value + "");
        }

        return component;
    };

    private static final Function<Integer, Component> SECONDS_FORMAT = (value) -> {
        Component component = Component.empty();

        if (value > 0) {
            component = MiniMessage.get().parse(MMOBuffs.getInst().getLanguageManager().getString("duration-display.seconds"), "value", value + "");
        }

        return component;
    };

    private static final Function<Duration, Component> DISPLAY_FORMAT = (duration) -> {
        List<Component> components = new LinkedList<>();

        components.add(DAYS_FORMAT.apply(duration.toDaysPart()));
        components.add(HOURS_FORMAT.apply(duration.toHoursPart()));
        components.add(MINUTES_FORMAT.apply(duration.toMinutesPart()));
        components.add(SECONDS_FORMAT.apply(duration.toSecondsPart()));
        components.removeIf(c -> c.equals(Component.empty()));


        TextComponent.Builder builder = Component.text();

        for (int i = 0; i < components.size(); i++) {
            builder.append(components.get(i));
            if (i != components.size()-1) {
                builder.append(Component.text(MMOBuffs.getInst().getLanguageManager().getString("duration-display.separator")));
            }
        }

        return builder.build();
    };

    private final ActiveStatusEffect effect;

    public TimedDisplay(ActiveStatusEffect effect) {
        this.effect = effect;
    }

    @Override
    public Component display() {
        return DISPLAY_FORMAT.apply(Duration.ofSeconds(effect.getDuration()));
    }
}
