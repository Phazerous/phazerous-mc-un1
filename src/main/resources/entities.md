# Entities

---

#### <u>Collection</u>: entities

The assortment holds a variety of models for entities meant to appear in the game world. These entities encompass
creatures, personalized armor stands, bosses, and more.

---

### BaseEntity structure

```
{
   "id": ObjectId;
   "title": String;
   "entityType": Int32;
   "respawnTime": Int64;
   "dropsIds": [ObjectId];
}
```

---

### GatheringEntity structure

```
{
   ...,
   hardness: Int64;
}
```

---

### MobEntity structure

```
{
   ...,
   "mobType": Int32,
   "maxHealth": Int64;
   "attack": Int64;
}
```

---