package com.ehhthan.mmobuffs.api.modifier;

// TODO: 1/6/2022 generalize so can be used with stacks
public enum Modifier {
    /**
     * Overwrites the existing effect under the same name with the one specified.
     * (For example, if you applied +10% PVE damage for 5 minutes, but want to replace
     * it with +15% PVE damage for 2 minutes with the same effect name, then it
     * will remove the effect and re-apply it with the new settings.)
     * Use this as default if not specified in the command.
     */
    REPLACE,

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
