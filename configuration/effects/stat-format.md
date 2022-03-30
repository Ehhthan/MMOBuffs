# Stat Format

### Stat Handler

Stat handlers control how stats are applied to the player. It provides a link between mmobuffs and other plugins that have stats.

#### Native Handlers

| Plugin                                                                                                                | Handler ID       |
| --------------------------------------------------------------------------------------------------------------------- | ---------------- |
| [MythicLib](https://mythiccraft.io/index.php?resources/mythiclib.403/)                                                | `mythiclib`      |
| [Aurelium Skills](https://www.spigotmc.org/resources/aurelium-skills-advanced-skills-stats-abilities-and-more.81069/) | `aureliumskills` |

{% hint style="info" %}
Handler IDs are important for specifying overlapping stats.
{% endhint %}

### Stat Configuration

{% hint style="success" %}
Stats can be negative and include decimals.
{% endhint %}

{% hint style="info" %}
You can use a `%` sign to make the stat a relative percentage change.
{% endhint %}

#### Example Config

```yaml
  stats:
    // The mythiclib handler id is used here, so it will be a mythiclib stat.
    mythiclib:attack_speed: 50%
    // The aureliumskills handler id is used here, so it will be a aureliumskills stat.
    aureliumskills:health: -3.5
    // No handler id is specified, so it will use the default in the config.yml
    movement_speed: 10
```
