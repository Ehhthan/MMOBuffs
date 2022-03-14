package com.ehhthan.mmobuffs.api.effect.stack;

public enum StackType {
    /**
     * When the effect duration expires, remove all stacks from the player.
     * This stack type will multiply <value> by the number of stacks.
     * Use this as default if not specified in the command.
     */
    NORMAL,

    /**
     * When the effect duration expires, remove one stack and then refresh the
     * duration. Repeat until no stacks are remaining. This stack type will
     * multiply <value> by the number of stacks.
     */
    CASCADING,

    /**
     * When the effect duration expires, remove one stack and then refresh the
     * duration. Repeat until no stacks are remaining. This stack type does not
     * multiply <value>.
     */
    TIMESTACK,

    /**
     * Removes a stack when the player attacks something. If no stacks are remaining,
     * the effect is removed. All stacks are removed if the effect duration runs out
     * naturally. This stack type does not multiply <value>.
     */
    ATTACK,

    /**
     * Removes a stack when the player is damaged by something. If no stacks are remaining,
     * the effect is removed. All stacks are removed if the effect duration runs out naturally.
     * This stack type does not multiply <value>.
     */
    HURT,

    /**
     * Removes a stack if the player both attacks something or damages something. (Basically
     * onAttack and onHit combined). Removed if duration runs out, and also does not multiply <value>
     */
    COMBAT

    // TODO: 1/6/2022 Concurrent stack type -> stacks time down at all at the same time.
}
