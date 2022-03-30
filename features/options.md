# Options

Options allow you to alter buffs based on in game events.

| Option               | Description                                 | Default Value |
| -------------------- | ------------------------------------------- | ------------- |
| keep-on-death        | If the effect will be removed on death.     | false         |
| keep-on-world-change | If the effect will be kept on world change. | true          |

#### Example Config Section

```yaml
  options:
    # The effect will be removed on death.
    keep-on-death: false
    # The effect will be kept on world change.
    keep-on-world-change: true
```
