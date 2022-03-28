# Effects

You can find an example of how the effects.yml looks.&#x20;

{% code title="effects.yml" %}
```yaml
strong:
  display-name: '<yellow>Strong'
  max-stacks: 4
  stack-type: normal
  stats:
    defense: 10%
    armor: 1
    max_health: 2
  display:
    icon: \uE040
    text: "<icon> <duration>"
damage:
  display-name: '<red>Damage'
  max-stacks: 4
  stack-type: normal
  stats:
    attack_damage: 3
  display:
    icon: \uE021
    text: "<icon> <red><duration>"
```
{% endcode %}

{% hint style="success" %}
You can have an unlimited amount of effects.
{% endhint %}

### Explanative Example

```yaml
# This is the id of the effect. You will use this in commands to reference this
# specific effect.
speedy:
  # The display name is how the effect looks when it is meant to be seen.
  # Default: Capitalized ID
  display-name: '<green>Speedy'
  # Options define what happens to this effect after certain events happen. If you
  # leave this section blank the options will take their defaults.
  options:
    # The effect will be removed on death.
    keep-on-death: false
    # The effect will be kept on world change.
    keep-on-world-change: true
  # The maximum amount of stacks that the effect can have. Default: 1
  max-stacks: 1
  # The way the stacks interact with the effect. Default: normal
  stack-type: normal
  
  # Which stats should be altered when the player has this effect. The stats can
  # be a positive or negative decimal number for a flat change or a percentage
  # for a relative change. If this section is omitted no stats will be applied.
  stats:
    attack_speed: 50%
    movement_speed: 10%
    attack_damage: -3.5
  # How the effect looks when in the bossbar. If this section is omitted this
  # effect will not be displayed.
    icon: \uE025
    text: "<icon> <duration>"
```
