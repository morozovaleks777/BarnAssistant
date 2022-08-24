package com.example.barnassistant.domain.repository

import androidx.lifecycle.LiveData
import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.model.BarnItemDB

interface RoomRepository {

        suspend fun deleteBarnItem(barnItem: BarnItem)
        suspend fun editBarnItem(barnItem: BarnItemDB)
        fun getAmount(list: List<BarnItem>):Float
        suspend fun getBarnItem(ItemId: Int): BarnItemDB
        fun getBarnList(): LiveData<List<BarnItem>>
        suspend fun addBarnItem(barnItem: BarnItem)
        suspend fun getCurrentTime():String


}