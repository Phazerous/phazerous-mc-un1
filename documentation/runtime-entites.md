# Runtime Entities

---

#### <u>Collection</u>: runtime_entities

The collection contains a depiction of a spawned entity, featuring customized attributes that facilitate convenient
access to its model characteristics and details regarding its present condition.

---

### BaseRuntimeEntity structure

```
{
   "id": ObjectId;
   "UUID": UUID;
   "locationedEntityId": ObjectId;
}
```

---

### MobRuntimeEntity structure

```
{
   "health": Double;
   "maxHealth": Double;
   "damage": Double;
}
```

---