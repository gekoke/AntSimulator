# Overview 🐜
A simple ant simulator that attempts to mimic and visualize the pheromone-based system some ant colonies in nature employ to find and retrieve food.

![picture of the app](ants.png)

# Build

```sh
gradle jar
```

# Run

*Requires `libxxf86vm.so` library on Linux.*

```sh
java -jar ./build/libs/AntSimulator.jar
```

# Usage
## Controls
| Key | Action |
| --- | ---    |
| **Mouse** | Place food chunks or walls |
| **A**     | Toggle ant visibility      |
| **W**     | Toggle between food/wall mode |
| **S**     | Increase simulation speed (4x) |
| **F**     | Toggle pheromone visibility |
| **P**     | Pause the simulation |

