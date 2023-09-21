# Items

---

## Item

---

#### <u>Collection</u>: items

The assortment holds a models of items that can be purchased/crafted/updated/etc.

---

### Item

```
{
   "id": ObjectId;
   "itemType": Integer;
   "materialType": Integer;
   "additionalMaterialType": Integer;
   "rarity": Integer;
}
```

| Rarity | Description |
|--------|-------------|
| 0      | Common      |
| 1      | Uncommon    |
| 2      | Rare        |
| 3      | Epic        |
| 4      | Legendary   |

| Item Type | Description       |
|-----------|-------------------|
| 0         | Usual             |
| 1         | Gathering Digging |
| 2         | Gathering Mining  |
| 3         | Weapon Handheld   |
| 4         | Armor             |"

- ItemType is stored in NBT.

---

> ### Gathering Item
> ```
> {
>  ...;
>  "speed": Long;
> }
> ``` 
> - Speed is the Long value that boost mining speed. The actual speed of gatherig is calculated
    as: <br/> `Gathering Time` = `Hardness` / `Speed` / `10`;
> - ItemSpeed is stored in NBT.
> ---
>
> ### Weapon Handheld
> ```
> {
>  ...;
>  "damage": Long;
> }
> ```
>
> ---
>
> ### Armor
> ```
> {
>  ...;
>  "defense": Long;
> }
> ```
>
> 64f498c7b4fd53dbfaf446ba
> 64f498e6b4fd53dbfaf446bd