# Gathering Module

The module is responsible for gathering resources from the world.

---

## Navigation

[1. Technical Implementation](./technical-implementation.md)

2. Logical Part
   1. [Vein](#vein)
   2. [VeinType](#veintype)
   3. [VeinDrop](#veindrop)
   4. [Resource](#resource)
   5. [VeinTool](#veintool)

### VeinLocation

`VeinLocation` — the location where the vein is spawned. Veins have many locationes, like ores
in the common game.

Stored in [Region](../regions/main.md) collection as a nested object.

```
{
   "_id": ObjectId;
   x: Double;
   y: Double;
   z: Double;
   veinId: ObjectId;
}
```

---

### Vein

`Vein` — the overall entity that holds information about the drops, durability, veinType.

Veins are entities and location where gathering mode starts. Each vein has unlimited amount of
resources, so gathering mode ends when the amount of player's turns are out.

Collection: `veins`

```
{
   "_id": ObjectId;
   "title": String;
   "veinType": Int32;    (VeinType)
   "resourceDurability": Int32;
   "drops": [{               (VeinDrop)
      "chance": Int32;    (0-10000, in code is divided by 100)
      "itemId: ObjectId;      (Item)
      "amount": {
         "min": Int32;
         "max": Int32;
      }
   }];,
   respawnTime: Int32;
}
```

[Item](../items/index.md)

---

### VeinType

`VeinType` — the type of the vein, it can be either `mining`, `digging` or `chopping`.

| Type   | Value | Tools Association                |
|--------|-------|----------------------------------|
| MINING | 0     | Pickaxe, Hammer, Crowbar, Chisel |

> For each veinType there is a separate set of tools that will be displayed when entering
> gathering mode. (e.g. for `mining` it will be a pickaxe, chisel, crowbar and hammer)

---

### VeinDrop

`VeinDrop` — the drop that will be dropped when each resource is broken.

---

### Resource

`Resource` — an individual instance of the vein, each vein has unlimited amount of resources.

Resource has its durability and every move the player makes, the durability is decreased by one.
When the durability is less than 0, the resource is broken, drop added to GUI and the next one is
spawned.

---

### VeinTool

`VeinTool` — the tool for gathering mode.

Collection: `tools`

```
{
   _id: ObjectId;
   title: String;
   materialType: Int32;
   toolType: Int32;
}
```

> Each tool has its type.

| ToolType | Value |
|----------|-------|
| PICKAXE  | 0     |
| HAMMER   | 1     |
| CROWBAR  | 2     |
| CHISEL   | 3     |

---

### PlayerVeinTool

`PlayerVeinTool` — the tool that a specific player has. It stored in player record

--- 

### VeinTypeToolsEnum

`VeinTypeToolsEnum` — the enum that holds the information about the toolsTypes for a `veinType`.

Collection: `enums`

```
{
   "_id": ObjectId,
   "enumKey": "vein_type_tools",
   "enumValue": {
      {
         "veinType": Int32;      (VeinType)
         "toolTypes": [Int32];   (ToolType)
      }
   }
}
```