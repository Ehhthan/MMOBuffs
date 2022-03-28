# Modifiers

Modifiers allow the user to determine how the applied effect will merge with the already existing effect.

| Modifier | Description                                                                                                                                                                                |
| -------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Set      | Overwrites the existing effect under the same name with the one specified or applies it if one does not exist already. This is the default option if one is not specified.                 |
| Refresh  | Refreshes the value of the effect, only if the remaining value on the player is lower than the value specified in the command. If the remaining value of the effect is higher, do nothing. |
| Add      | Adds the specified amount to the remaining value of the effect. This also keeps the value on the player and ignores the one specified in the command.                                      |
| Subtract | Subtracts the specified amount from the remaining value of the effect. This also keeps the value on the player and ignores the one specified in the command.                               |
| Keep     | If the player already has the specified effect value, do not modify anything. If the player does not have this effect, then apply it normally.                                             |
