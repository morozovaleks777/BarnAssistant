package com.example.barnassistant.domain.repository

import com.example.barnassistant.data.DataOrException
import com.example.barnassistant.data.Resource
import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.NameBarnItemList
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
suspend fun deleteBarnItemList(name:Int)
        suspend fun deleteBarnItem(barnItem: BarnItem)
        suspend fun editBarnItem(barnItem: BarnItemDB)
        fun getAmount(list: List<BarnItemDB>):Float
        suspend fun getBarnItem(ItemId: Int): BarnItemDB
        fun getBarnItemDBList(): Flow<List<BarnItemDB>>
        suspend fun addBarnItem(barnItem: BarnItem)
        suspend fun getCurrentTime():String

suspend fun getItem(itemId:Int):Resource<BarnItemDB>
    fun getItemDBList():Resource< Flow<List<BarnItemDB>>>

suspend fun addName(nameBarnItemList: NameBarnItemList)

//                suspend fun < T>getAllNameBarnItemListFromDatabase(): DataOrException<List<T>, Boolean, Exception>
//
//                {
//                        val dataOrException = DataOrException<List<T>, Boolean, Exception>()
//
//                        try {
//                                dataOrException.loading = true
//                                dataOrException.data =  queryBook.get().await().documents.map { documentSnapshot ->
//                                        documentSnapshot.toObject(T::class.java)!!
//                                }
//                                if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false
//
//
//                        }catch (exception: FirebaseFirestoreException){
//                                dataOrException.e = exception
//                        }
//                        return dataOrException
//
//                }

 fun getList():Flow<List<NameBarnItemList>>
 //suspend fun getBarnItemName(nameBarnItemList: NameBarnItemList):NameBarnItemList
 suspend fun getBarnItemName(name: String):NameBarnItemList

    suspend fun getBarnItemNameFromName(name:String): NameBarnItemList
    suspend fun deleteNameBarnItem(nameBarnItemList: NameBarnItemList)
    suspend fun deleteNameBarnItem2(nameBarnItemList: NameBarnItemList)
}