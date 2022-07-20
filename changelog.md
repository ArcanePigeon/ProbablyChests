# Changelog

All notable changes to this project will be documented in this file.

## [0.1.0] - 4/4/2022
### Added
- Lush Chest
- Clay Pot

## [0.2.0] - 4/7/2022
### Added
- Normal Chest
- Rocky Chest
- Lush Pot
- Rocky Pot
- Nether Pot
- New loot tables
- Biome specific spawning
- Spawn rate config
- Made Pots silk touchable

## [0.3.0] - 4/12/2022
### Added
- Stone Chest
- Gold Chest
- Mimics
- Mimic keys
- Mimic Key Fragments
### Changed
- New chest spawn mechanics
- Config options for rarity are now a percent float value
- New snazzy icon
- The proper chest types now mine faster with right tools

## [0.3.1] - 4/17/2022
### Fixed
- Fixed aether crash

## [0.3.2] - 4/21/2022
### Added
- Pet Mimics
- Pet Mimic Key
- Mimic Core
### Changed
- When a chest becomes a mimic or pet mimic its inventory will be stored in the entity instead of dropping
### Fixed
- Fixed crash when setting any spawn chance in the config to 0

## [0.4.0] - 5/2/2022
### Added
- Mimics now spawn naturally
- Config option to have easier mimics
- Config option for naturally spawning mimics
- Config option for natural mimic spawn rate
### Changed
- Decreased chance of mimic when opening chest
- Mimics no longer drown
- Increased chance to find mimic key fragments in chests
- Mimics can now drop key fragments
- Pet mimics now have different animation for sitting and standing
### Fixed
- Fixed issue where opening a mimic with a bow would cause bow to fire when the gui was closed
- Fixed pet mimics were not allowed in peaceful

## [0.4.1] - 5/2/2022
### Fixed
- Fixed issue with mimics spawning too frequently
- Fixed issue with mimics spawning in oceans
- Fixed issue where mimics did not spawn in their proper biomes

## [0.4.2] - 5/3/2022
### Added
- Config option to disable the creation of pet mimics
### Changed
- Sneaking while using a mimic key or pet mimic key will not use the key
### Fixed
- Fixed issue where using a pet mimic key on a chest that was marked as a mimic already would create 1 mimic and 1 pet mimic
- Fixed issue with pet mimic animations and sitting state not updating correctly upon creation

## [0.4.3] - 5/9/2022
### Fixed
- Fixed issue where mimics spawn in biomes they shouldn't
- Fixed issue where mimics spawn in the air
- fixed issue where mimics spawn way too frequently

## [0.5.0] - 7/19/2022
### Added
- Cloth Config compat
- Mod Menu compat
- Option to limit the number of pet mimics a player can have
- Overhauled animations for all mimics
- New sounds
- Custom screens for chests and mimics that can be modified with a texture pack
- Mimic hand bell
- Mimic abandoning and reclaiming
- Pet mimics now fight for the player like tamed wolves
- Tooltips for most items
- Crafting recipes for all chests
- Keys and locks for locking pet mimics and chests
- Gold and Void chests spawn locked and require their respective keys
- Ability to lock pet mimics
- Locked pet mimics bite other player who try to access them
- More difficulty options for wild mimics
- Advancements
- Statistics
- Molten chest
- Void chest
- Ice chest
- Coral chest
- Coral chests emit light underwater
- Ice and Molten chests give off a low level of light
- Locked chests are unbreakable
- Configs for new locking and abandonment mechanics
### Changed
- Lush chest texture
- Renamed mimic key to suspicious key
- Renamed mimic core to suspicious eye
- Renamed pet mimic key to friendly key
- Renamed stone chest to deepslate chest
- Renamed rocky chest to sandstone chest
- Suspicious key texture
- Key fragments texture
- Friendly mimic key texture
- Suspicious eye texture
- Overhauled loot tables to be less overpowered
- Overhauled world gen so chests are more spaced out and generate at the proper levels and biomes
- Chests cannot be destroyed be explosions
### Fixed
- Chest animation bug
- Loud mimic sounds
- Bad lighting check for spawning
- Collision shape for chests does not rotate
- Mimics could attack through shields sometimes
