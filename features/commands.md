---
description: The commands of the plugin.
---

# Commands

Commands are essential for applying, removing, and altering a player's effect.

| Command                                                                                                    | Description                                                                       | Permission         |
| ---------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------- | ------------------ |
| /mmobuffs reload                                                                                           | Reload the plugin.                                                                | mmobuffs.reload    |
| /buff give \<player> \<effect> \<duration> <[modifier](modifiers.md)> \<stacks> <[modifier](modifiers.md)> | Give an effect to a player.                                                       | mmobuffs.give      |
| /buff permanent \<player> \<effect> <[modifier](modifiers.md)> \<stacks> <[modifier](modifiers.md)>        | Give a permanent effect to a player.                                              | mmobuffs.permanent |
| /buff clear \<player> \<effect\|all\|permanent>                                                            | Remove a single effect, all non permanent effects, or every effect from a player. | mmobuffs.clear     |
| /buff stack \<player> \<effect> \<set\|add\|subtract\|multiply\|divide> \<stacks>                          | Alter the stacks of an effect.                                                    | mmobuffs.stack     |
| /buff time \<player> \<effect> \<set\|add\|subtract\|multiply\|divide> \<duration>                         | Alter the duration of an effect.                                                  | mmobuffs.time      |
| /buff list \[player]                                                                                       | List the current effects on a player.                                             | mmobuffs.list      |

{% hint style="info" %}
If you end the command with `-s`it will execute silently.
{% endhint %}
