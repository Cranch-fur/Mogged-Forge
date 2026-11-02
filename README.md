<p align="center">
  <b>English</b> · <a href="README_RU.md">Русский</a>
</p>

# Mogged for Forge

A port of [Mogged](https://github.com/Cranch-fur/Mogged) to [Minecraft Forge](https://files.minecraftforge.net/net/minecraftforge/forge/). It does the
same things: lets you switch between your own account skin and two custom presets with a single key, with a smooth animated transition. HD skins, capes and elytra, a built-in skin editor, and other players who have the mod see your look too.

The only difference from the Fabric mod: no [Flashback](https://modrinth.com/mod/flashback) support, as that replay mod
exists for Fabric only.

## Requirements

| Minecraft | [Forge](https://files.minecraftforge.net/net/minecraftforge/forge/) | Java |
|---|---|---|
| 1.20.1 | 47.4+ | 17 |
| 1.21.1 | 52.1+ | 21 |
| 26.1.x | 62+ | 25 |
| 26.2 | 65+ | 25 |
| 26.3 | 66+ | 25 |

Each version needs its own jar. To let other players see your presets, put the same jar on the server. A vanilla server
works too, but then only you see them. Fabric and Forge players don't see each other's presets: the loaders use
different network channels.

## Building

Any JDK 17+ starts Gradle; the JDK a version needs (17, 21 or 25) is downloaded automatically.

```sh
./gradlew build            # Minecraft 26.2 (default)
./gradlew build -Pmc=26.1
./gradlew build -Pmc=26.3
./gradlew build -Pmc=1.21.1
./gradlew build -Pmc=1.20.1
```

On Windows use `gradlew.bat`. The jar ends up in `build/libs/`, for example `mogged-1.1.0+mc26.2-forge.jar`. For 1.20.1
there is also a `…-dev.jar` with Mojang names, for development only: put the jar without `-dev` in the game.

Run the game from the workspace with `./gradlew runClient -Pmc=<version>`.

## Project layout

```
src/main/java/mogged          shared code: protocol and the server-side relay
src/client/java/mogged/client shared client code (as in the Fabric mod)
  ├─ gui                      screens, widgets and the skin editor
  ├─ render                   CPU model renderer for previews
  └─ compat                   a Flashback stand-in (there is none on Forge)
src/mc26, src/mc1211, src/mc1201
                              per-version code: the "platform" classes (networking, drawing, input, textures),
                              the mixins into player skin lookup and the Forge event glue (mogged/forge)
versions/                     per-Minecraft-version build settings; "family" picks the folder above
```

Only the loader glue and networking differ from the Fabric mod: `mogged/forge` (the `@Mod` entry point and Forge
events), `platform/ModNetworking` and `client/platform/ClientNetworking` (Forge channels), `MoggedClient` (no Fabric API)
and `SkinServer` (its events moved out). Everything else (skins, the animation, screens, the editor) is copied as is.

Settings are stored in `config/mogged.json`, in the Fabric mod's format.

## Authors

@cranch_fur and @murkatel.

## License

Copyright (C) 2026 @cranch_fur, @murkatel

Mogged is free software under the [GNU General Public License v3.0](LICENSE).
