# Collections

---

### Custom Inventories

<ins><b>Collection</b></ins>: custom_inventories

### Structure

```
{
   "id": ObjectId;
   "title": String;
   "size": Number;
   "contents": [
      {
         "itemId": ObjectId;
         "slots": [Number];
         "actionId: ObjectId; 
         }
      }
   ]
}
```

When the inventory is opened, the contents are loaded from the database and saved to HashMap.

When clicking a specific item in the inventory, the actionId is used to find the action in the database.
The action is stored in Hashmap and executed.

Size must be a multiple of 9 and less or equal than 54.

---

### Custom Inventory Actions

<ins><b>Collection</b></ins>: custom_inventory_actions

Action types: `trade`

`trade`

```
{
   "id": ObjectId;
   "type": "trade";
   "itemId": ObjectId;
   "price": Double;
}
```

Action is executed when the player clicks the item in the [inventory](#custom-inventories).