package com.example.barnassistant.data

import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.BarnItemFB
import javax.inject.Inject

class BarnListMapper @Inject constructor(){
    fun mapBarnItemToBarnItemDB(barnItem: BarnItem): BarnItemDB {
return BarnItemDB(
    itemId = barnItem.itemId,
    name = barnItem.name,
    count = barnItem.count,
    price = barnItem.price,
    enabled = barnItem.enabled,
    listName = barnItem.listName,
    time = barnItem.time,
    isInFirebase = barnItem.isInFirebase
)
    }

    fun mapBarnDBToBarnItem(barnItemDB: BarnItemDB):BarnItem{
        return BarnItem(
            itemId = barnItemDB.itemId,
            name = barnItemDB.name,
            count = barnItemDB.count,
            price = barnItemDB.price,
            enabled = barnItemDB.enabled,
            listName = barnItemDB.listName,
            time = barnItemDB.time,
            isInFirebase = barnItemDB.isInFirebase
        )
    }
    fun mapBarnDBToBarnItemFB(barnItemDB: BarnItemDB): BarnItemFB {
        return BarnItemFB(
            itemId = barnItemDB.itemId,
            name = barnItemDB.name,
            count = barnItemDB.count,
            price = barnItemDB.price,
            enabled = barnItemDB.enabled,
            listName = barnItemDB.listName,
            time = barnItemDB.time,
            isInFirebase = barnItemDB.isInFirebase
        )
    }
    fun mapBarnFBToBarnItemDB(barnItemFB: BarnItemFB): BarnItemDB {
        return BarnItemDB(
            itemId = barnItemFB.itemId?:0,
            name = barnItemFB.name?:"",
            count = barnItemFB.count?:0f,
            price = barnItemFB.price?:0f,
            enabled = barnItemFB.enabled?:false,
            listName = barnItemFB.listName?:"",
            time = barnItemFB.time?:"",
            isInFirebase = barnItemFB.isInFirebase ?:false
        )
    }

    fun mapListBarnItemDBToListBarnItem(list:List<BarnItemDB>)=
        list.map {
            mapBarnDBToBarnItem(it)
        }
    }
