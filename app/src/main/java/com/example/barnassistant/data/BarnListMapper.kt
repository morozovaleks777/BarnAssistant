package com.example.barnassistant.data

import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.model.BarnItemDB
import javax.inject.Inject

class BarnListMapper @Inject constructor(){
    fun mapBarnItemToBarnItemDB(barnItem: BarnItem): BarnItemDB {
return BarnItemDB(
    itemId = barnItem.itemId,
    name = barnItem.name,
    count = barnItem.count,
    price = barnItem.price,
    enabled = barnItem.enabled,
    listName = barnItem.listName
)
    }

    fun mapBarnDBToBarnItem(barnItemDB: BarnItemDB):BarnItem{
        return BarnItem(
            itemId = barnItemDB.itemId,
            name = barnItemDB.name,
            count = barnItemDB.count,
            price = barnItemDB.price,
            enabled = barnItemDB.enabled,
            listName = barnItemDB.listName
        )
    }

    fun mapListBarnItemDBToListBarnItem(list:List<BarnItemDB>)=
        list.map {
            mapBarnDBToBarnItem(it)
        }
    }
