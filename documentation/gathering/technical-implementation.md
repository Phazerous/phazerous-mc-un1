# Logic

## Navigation:

[← Main](./main.md)

* [1. Overall](#overall)
* [2. Spawning Veins](#spawning-veins)
* [3. GUI](#gui)

---

### Overall

When a user enters a specific veinLocation in the world, the module spawns armor stands that will be
used to display the gathering resource. The armor stands are sent for individual person, so it uses
direct minecraft packets to spawn them. When the user interacts with a resource the special
inventory is opened where the mini-game takes place. The game design is that you will have maximum
five different items (including hand-item)in order to "mine". The player will have
specified amount of turns to break blocks. Each block consists of multiple layers, and it has
overall durability, when the block is broken, the next one is spawned in the inventory and this will
happen until player's turns run out. Each layer will be one of four types of layers and for each
layer the special item will be more efficient, so breaking more block durability than others.
When the one block is broken, the reward will be added in the same GUI and the new block will be
spawned and so on, until turns are out. When the turns are out, the special item will be
appeared in GUI and by clicking it all resources will be added to the inventory and the GUI will
close.

The layer that is placed in the middle will display the percentage of the block durability left.
When the durability is greater than 64, the block size will be 64. The block will display the
durability left, durability total and broken percentage. Each

---

### Spawning Veins

When the player enters a specific veinLocation, the veins are spawned based on VeinLocations. So,
each veinLocation has veins property that holds the list of veins that are spawned in this
veinLocation.
Veins are building by getting all VeinLocations and for each VeinLocation the Vein is found.
Then the veins are spawned for individual person, so using NMS Packets. All the veins are stored
in memory, so when the player leaves the server everything just disappears, because it didn't
exist for others. When the player leaves the server or the veinLocation, the veins are removed from
map and also memory.

Veins are just armor stands that are spawned in the world, when spawning the special entityId is
saved for a specific person, while he is in the veinLocation. The special listeners check all NMS
Packets and when some interaction is made it checks if entityId is in the player's veins. If so
then the GUI inventory opens for the player.

---

### GUI

When users interact with the armor stand, the GUI is opened. The GUI is custom inventory that
has 3 building stages:

1. Building the inventory pattern from only the code. (Placing background)
2. Retrieving the vein information from the database, building first layer and providing the
   percentage information, durability, description (like effectiveTool). The current resource is
   stored for a player.
3. Based on `VeinType` (getting toolsIds from enum) get the `VeinTools` of
   the player from his db record and place them in the inventory. Each vein type has maximum of 4
   tools, some or all the tools can be absent. In gathering mode user always have a hand item, so
   player can have no tools at all.

`VeinTools` are placed in the bottom slots of the inventory, always in the same position, but in
different order, that generates randomly. This is designed in order to decrease the chance of
exploit the gathering mechanics.

When tools are being set in the inventory, each placement is registered in memory, so to know
that at pos1 there should be tool1, at pos2 tool2, etc. So, in order not to always request the
item from memory or even db.

Player is only available to use the tools in the inventory, nothing else is allowed and will not
count as a turn. When player clicks something else, the disapproving sound is played.

When player clicks an item, the manager finds the `VeinTool` that is assigned to the slot, then
it checks the current layer, `resource` durability and then performs actions:

1. Checks the layer effectiveTool and compares it with the tool that was clicked. If they are
   equal, then the tool is effective and the durability is decreased by `tool_power` and
   future bonuses, otherwise the durability is decreased by `tool_power * 0,5`, bonuses
   aren't applied and the player loses one additional turn.
2. If the durability is greater than 0, then the layer updates, changing the layer and updating
   `resource` information (durability, percentage, etc.)
3. If the durability is less than 0, then the resource breaks, drops are calculated and added to
   the GUI (upper slots) the next resource is spawned and the layer is updated.
4. If turns are out, then the special item is added to the inventory (in the layer's veinLocation)
   and by clicking it, all resources are added to the inventory and the GUI is closed.

---