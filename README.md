# Preferred Gamerules

A lightweight mod for customizing the default values of gamerules, i.e the values that are pre-selected when creating a new world.

Preferences are saved in `.minecraft/config/preferred-gamerules.properties`.
Each gamerule name is prefixed with `gamerule.`.

Example:
```
gamerule.minecraft:fire_damage=false
gamerule.minecraft:keep_inventory=true
```

Preferences can also be edited in-game via **ModMenu**, if installed. In this case, only values that are different from vanilla's default will be written to the config.
