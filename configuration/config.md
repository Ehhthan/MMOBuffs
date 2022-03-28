# Config

```yaml
resource-pack:
  # If enabled will require the use of the resource pack.
  enabled: true
  # The custom font to be used to display the formatting for effects.
  font: "mmobuffs:default"

# Set the settings of the bossbar display here.
bossbar-display:
  enabled: true

  effect-separator: " "

  # Possible colors: white, purple, yellow, green, red, blue, and pink.
  color: 'white'

  # Possible overlays: progress, notched_6, notched_10, notched_12, notched_20.
  overlay: 'progress'

  # Default percentage value of the bossbar. Value must be between [0,1].
  value: 0

# How effects are sorted when displayed.
sorting:
  # If true the effects with the lowest duration left will be displayed first.
  duration-ascending: true

# Whether to use a shortened display.
shorten-duration-display: true

config-version: 2
```