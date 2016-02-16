Actions
-------

### ActionDeleteWorld
`chikachi.interactive.common.action.implementation.ActionDeleteWorld`<br>
Kicks all players and deletes the world.

###### Data
No data options

----------

### ActionDropItem
`chikachi.interactive.common.action.implementation.ActionDropItem`<br>
Drops items from the players inventory.

###### Data
| Key             | Value                                 | Description                             |
|-----------------|---------------------------------------|-----------------------------------------|
| type (optional) | "HOLDING", "ALL", "ARMOR" or "RANDOM" | "HOLDING" drops the current holding item<br>"ALL" drops all items<br>"ARMOR" drops all armor<br>"RANDOM" drops a single random item |

### ActionGive
`chikachi.interactive.common.action.implementation.ActionGive`<br>
Gives an item to the player.

###### Data
| Key               | Value          | Description                                         |
|-------------------|----------------|-----------------------------------------------------|
| item              | modId:itemName | For example "minecraft:stone"                       |
| amount (optional) | number         | Amount of item to give the player<br>**Default:** 1 |

----------

### ActionHeal
`chikachi.interactive.common.action.implementation.ActionHeal`<br>
Heals the player.

###### Data
| Key               | Value   | Description                                                              |
|-------------------|---------|--------------------------------------------------------------------------|
| saturation        | boolean | If the players food and saturation should be maxed<br>**Default:** false |

----------

### ActionKick
`chikachi.interactive.common.action.implementation.ActionKick`<br>
Kicks the player.

###### Data
No data options

----------

### ActionPlaySound
`chikachi.interactive.common.action.implementation.ActionPlaySound`<br>
Plays a sound at the players position.

###### Data
| Key               | Value   | Description                                                              |
|-------------------|---------|--------------------------------------------------------------------------|
| sound             | string  | Sound name, for example "creeper.primed" for the creeper sound           |
| volume            | double  | Volume (between 0 and 10)                                                |
| pitch             | double  | Pitch (between 0 and 2)                                                  |

----------

### ActionPotion
`chikachi.interactive.common.action.implementation.ActionPotion`<br>
Gives the player a potion effect.

###### Data
| Key               | Value   | Description                                                              |
|-------------------|---------|--------------------------------------------------------------------------|
| id                | number  | Potion ID                                                                |
| stackable         | boolean | If potion effects can stack in duration and amplifier                    |
| duration          | number  | Duration (at least 1)                                                    |
| amplifier         | number  | Amplifier (at least 1)                                                   |

----------

### ActionRandomItem
`chikachi.interactive.common.action.implementation.ActionRandomItem`<br>
Gives the player a random item. 

###### Data
No data options

----------

### ActionRandomPotion
`chikachi.interactive.common.action.implementation.ActionRandomPotion`<br>
Gives the player a random potion effect.

###### Data
| Key               | Value   | Description                                                              |
|-------------------|---------|--------------------------------------------------------------------------|
| positive          | boolean | Give positive potion effects                                             |
| negative          | boolean | Give negative potion effects                                             |
| stackable         | boolean | If potion effects can stack in duration and amplifier                    |
| duration          | number  | Duration (at least 1)                                                    |
| amplifier         | number  | Amplifier (at least 1)                                                   |

----------

### ActionStructure
`chikachi.interactive.common.action.implementation.ActionStructure`<br>
**DO NOT USE - WIP** 

###### Data
No data options

----------

### ActionSummon
`chikachi.interactive.common.action.implementation.ActionSummon`<br>
Summon an entity by the player.

###### Data
| Key               | Value   | Description                                                              |
|-------------------|---------|--------------------------------------------------------------------------|
| entity            | string  | Entity name, for example creeper                                         |
| amount            | number  | Amount of entities to spawn                                              |
| radius            | number  | Radius around the player to spawn within                                 |

----------

### ActionTeleport
`chikachi.interactive.common.action.implementation.ActionTeleport`<br>
Teleports the player to a dimension.<br>
Will teleport to spawn point unless specific coordinates is set.<br>
**NOTE:** Teleport to spawn in other dimensions than Overworld is not recommended.

###### Data
| Key               | Value   | Description                                                              |
|-------------------|---------|--------------------------------------------------------------------------|
| dimension         | number  | Dimension.<br>**Default:** 0 (Overworld)                                 |
| x (optional)      | number  | x-coordinate<br>Ignored if `y` and `z` isn't set                         |
| y (optional)      | number  | y-coordinate<br>Ignored if `x` and `z` isn't set                         |
| z (optional)      | number  | z-coordinate<br>Ignored if `x` and `y` isn't set                         |

----------

### ActionTest
`chikachi.interactive.common.action.implementation.ActionTest`<br>
Writes a test message in chat.

###### Data
No data options

----------

### ActionTime
`chikachi.interactive.common.action.implementation.ActionTime`<br>
Changes time in the world. 

###### Data
| Key               | Value   | Description                                                              |
|-------------------|---------|--------------------------------------------------------------------------|
| addition          | boolean | If the `time` should be added to current time                            |
| time              | number  | Amount of time to add or specific time to set                            |
