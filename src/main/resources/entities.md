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
   "drops": [EntityDrop];
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
   "moneyReward": MoneyReward;
}
```

- ### BossEntity structure

* ```
  {
       ...,
       "minions: [ObjectId];
  }
  ```

-
   - Minions are LocationedEntities.

<br />
<br />
<br />

## Schemas

---

### EntityDrop

```
{
   "dropChance": Double;
   "itemId": ObjectId;
}
```

- The dropChance is a value between 0 and 1, where 0 is 0% and 1 is 100%.

- If the dropChance not provided, then the item will be drop 100% of the time.

---

### MoneyReward

```
{
   "min": Double;
   "max": Double;
}
```