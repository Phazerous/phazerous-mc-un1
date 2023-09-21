# Enums

---

#### <u>Collection</u>: enums

The assortment holds a variety of enums that are used in the game.

---

## Gathering Resources

```
{
   "_id": "ObjectId",
   "enumKey": "gathering_resources",
   "enumValues": [
      {
         "title": "string",
         "type": "string",
         "materialType": "string",
         "description": "string",
         "effectiveToolType": "int"
      }
   ]
}
```

- Type can be either "mining", "digging" or "chopping".
- EffectiveToolType is the type of tool that is effective against the block. It is stored in NBT.