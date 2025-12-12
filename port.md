# Minecraft Code Breaking Changes
### 1.19.4
Initial Release

### 1.21.2
#### No Workaround:
- `Gamerules.accept` is no longer static
- `Gamerules::new` Now requires feature flags

### 1.21.11
- Gamerules are now registered via the registry.
- Most subtypes (`Rule`, `Type`, `Key`) are gone.
