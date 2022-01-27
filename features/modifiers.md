# Modifiers

Modifiers allow the user to determine how the applied effect will merge with the already existing effect.

| Modifier | Description                                                                                                                                                                                        |
| -------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Replace  | Overwrites the existing effect under the same name with the one specified or applies it if one does not exist already. This is the default option if one is not specified.                         |
| Refresh  | Refreshes the duration of the effect, only if the remaining duration on the player is lower than the time specified in the command. If the remaining duration of the effect is higher, do nothing. |
| Add      | Adds the specified time to the remaining duration of the effect. This also keeps the value on the player and ignores the one specified in the command.                                             |
| Subtract | Subtracts the specified time from the remaining duration of the effect. This also keeps the value on the player and ignores the one specified in the command.                                      |
| Keep     | If the player already has the specified effect, do not modify anything. If the player does not have this effect, then apply it normally.                                                           |
