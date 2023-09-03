# GUI

---

> To create a custom graphical user interface within Minecraft using the inventory system, you must begin by defining
> the inventory itself. Each item within this inventory can be associated with an action ID, which dictates what action
> should be taken when a player interacts with the item. <br /><br />
> For instance, if you want a specific action to occur when a player selects an item in your custom inventory, such as
> purchasing an item, you need to define this behavior. To achieve this, you retrieve the action associated with the
> item
> from the inventory's description. If an action exists, you can specify particular options for it. For example, you can
> set an additional price for an item within your inventory and associate it with an action ID in string
> format. <br /><br />
> To determine if a specific item has an associated action ID and execute the corresponding action, you utilize an
> action manager. This manager handles the execution of actions based on the provided action IDs, enabling your custom
> graphical user interface to respond appropriately to player interactions within the inventory. <br />

## GUI Inventories

---

#### <u>Collection</u>: gui_inventories

The assortment holds a description of the inventory that should be created.

---

### GUI Inventory Structure

```
{
   "id": ObjectId;
   "title": String;
   "size": Integer;
   "contents": [GUIItem];
}
```

- When the inventory is opened, the contents are loaded from the database and saved to HashMap.

- When clicking a specific item in the inventory, the actionId is used to find the action in the database.
  The action is stored in Hashmap and executed.

- Size must be a multiple of 9 and less or equal than 54.

<br />
<br />
<br />

## GUI Actions

---

#### <u>Collection</u>: gui_actions

The assortment holds a description of the action that should be executed.

---

### GUI Action (abst.)

```
{
   "id": ObjectId;
   "type": Integer;
}
```

| Type | Description         |
|------|---------------------|
| 1    | Purchase with money |
| 2    | Purchase with item  |

---

> ### Purchase Item Action (abst.)
> ```
> {
>   ...;
>   "itemIdToPurchase": ObjectId;
> }
> ```
> > #### Purchase Item with Money Action (type = 1)
> > ```
> > {
> >   ...;
> >   "price": Double;
> > }
> > ```
> > 24
> > #### Purchase Item with Item Action (type = 2)
> > ```
> > {
> >   ...;
> > "requestedItemId": ObjectId;
> > 
> > "amount": Integer;
> > }
> > ```

<br />
<br />
<br />

## Schemas

---

### GUI Item

```
{
   "itemId": ObjectId;
   "slots": [Integer];
   "actionId": ObjectId;
}
```
