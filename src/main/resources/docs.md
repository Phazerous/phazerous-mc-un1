### CustomGUI

Database structure

**inventories**

```
{
  "Id": ObjectId;
  "title": String;
  "size": Number; (9, 18, 27, 36, 45, 54)
  "contents": [
    {
        "itemId": ObjectId;
        "slots": [Number];
    ]...
  }
}
```

When the inventory is opened, the contents are loaded from the database and saved to HashMap.