package com.ehhthan.mmobuffs.api.stat;

import org.jetbrains.annotations.NotNull;

public class StatValue {
    private final double value;
    private final ValueType type;

    public StatValue(@NotNull String value) {
        if (value.endsWith("%")) {
            value = value.substring(0, value.length() - 1);
            this.type = ValueType.RELATIVE;
        } else {
            this.type = ValueType.FLAT;
        }

        try {
            this.value = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Stat value '" + value + "' is not a valid number.");
        }
    }

    public StatValue(double value) {
        this(value, ValueType.FLAT);
    }

    public StatValue(double value, @NotNull ValueType type) {
        this.value = value;
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public @NotNull ValueType getType() {
        return type;
    }

    @Override
    public @NotNull String toString() {
        return String.valueOf(
            switch (type) {
                case FLAT -> value;
                case RELATIVE -> value + '%';
            }
        );
    }

    public enum ValueType {
        FLAT,
        RELATIVE
    }
}
