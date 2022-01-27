# Stack Types

Stack types determine how the stacks of an effect function.

| Stack Type | Description                                                                                                                                                                                                            |
| ---------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Normal     | When the effect duration expires, remove all stacks from the player. This stack type will multiply value by the number of stacks. This is the default type used if not specified.                                      |
| Cascading  | When the effect duration expires, remove one stack and then refresh the duration. Repeat until no stacks are remaining. This stack type will multiply value by the number of stacks.                                   |
| Timestack  | When the effect duration expires, remove one stack and then refresh the duration. Repeat until no stacks are remaining. This stack type does not multiply value.                                                       |
| Attack     | Removes a stack when the player attacks something. If no stacks are remaining, the effect is removed. All stacks are removed if the effect duration runs out naturally. This stack type does not multiply value.       |
| Hurt       | Removes a stack when the player is damaged by something. If no stacks are remaining, the effect is removed. All stacks are removed if the effect duration runs out naturally. This stack type does not multiply value. |
| Combat     | Removes a stack if the player is either hurt by an entity or damages another entity. All stacks are removed if the effect duration runs out naturally. This stack type does not multiply value.                        |
