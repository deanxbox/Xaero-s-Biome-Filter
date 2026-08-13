# Xaero's Biome Filter

A client-side Fabric mod for Minecraft Java Edition 26.2 that adds biome filtering to Xaero's World Map.

## Features

- Opens directly from the Xaero's World Map toolbar.
- Pauses single-player while the biome menu is open.
- Lists every biome in the active registry, including modded biomes.
- Uses localized, friendly biome names such as `Plains` and `Forest`.
- Searches by friendly name or full biome identifier.
- **Show selected** mode blacks out every biome that is not selected.
- **Hide selected** mode blacks out selected biomes.
- Applies World Map filtering as an instant render-time overlay.
- Supports Xaero's Minimap when it is installed.

## Requirements

- Minecraft Java Edition 26.2
- Fabric Loader 0.19.3 or newer
- Fabric API for Minecraft 26.2 (required by Xaero's World Map)
- Xaero's World Map 1.42.0 or newer for Minecraft 26.2
- Java 25
- Optional: Xaero's Minimap 26.2.0 or newer for Minecraft 26.2

Development builds are tested against Xaero's World Map 1.44.2 and Xaero's Minimap 26.4.2.

## Installation

1. Install Fabric Loader and the required Xaero's World Map version.
2. Place the Xaero's Biome Filter jar in the Minecraft `mods` folder.
3. Open Xaero's World Map and select the biome-filter button from its toolbar.

The configuration is stored in `config/xaeros-biome-filter.json`.

## Building

```powershell
.\gradlew.bat clean build
```

The distributable jar is generated in `build/libs`.

## Disclaimer

This project is an unofficial add-on and is not affiliated with or endorsed by Xaero.

## License

MIT
