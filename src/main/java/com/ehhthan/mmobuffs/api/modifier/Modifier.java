package com.ehhthan.mmobuffs.api.modifier;

// TODO: 1/6/2022 generalize so can be used with stacks
public enum Modifier {
    /**
     * Overwrites the existing value.
     */
    SET,

    /**
     * If the player already has the specified effect, do not modify anything. If the player
     * does not have this effect, then apply it normally.
     */
    KEEP,

    /**
     * Refreshes the duration of the effect, only if the remaining duration on the player
     * is lower than the time specified in the command. If the remaining duration of the
     * effect is higher, do nothing. If a different <value> is specified in this command,
     * ignore it and keep the one already on the player.
     */
    REFRESH,

    /**
     * Adds the specified time to the remaining duration of the effect. This also keeps
     * the <value> on the player and ignores the one specified in the command.
     */
    ADD,

    /**
     * Subtracts the specified time from the remaining duration of the effect. This also keeps
     * the <value> on the player and ignores the one specified in the command.
     */
    SUBTRACT
}
