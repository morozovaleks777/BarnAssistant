package com.example.barnassistant.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.NameBarnItemList
import kotlinx.coroutines.flow.Flow



@Dao
interface RoomDao {
    @Query("SELECT * from barn_tbl")
    fun getBarnList(): Flow<List<BarnItemDB>>

//    @Query("SELECT * from barn_tbl where listName =:listName")
//    suspend fun getFavById(listName: String): BarnItemDB

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBarnItem(barnItemDB: BarnItemDB)



//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun updateBarnList(barnItem: BarnItem)

    @Query("DELETE from barn_tbl")
    suspend fun deleteAllBarnItemFromList()

    @Query("DELETE FROM barn_tbl WHERE itemId=:barnItemId")
    suspend fun deleteBarnItem(barnItemId:Int)

//    @Query("SELECT * FROM barn_tbl")
//    fun getBarnItemList(): LiveData<List<BarnItemDB>>


    @Query("SELECT * FROM barn_tbl WHERE itemId=:barnItemId LIMIT 1")
    suspend fun getBarnItem(barnItemId:Int):BarnItemDB

    @Query("SELECT * FROM barn_tbl WHERE listName=:listName LIMIT 1")
   fun getNeededList(listName:String):LiveData<List<BarnItemDB>>


   //NameBarnItemList

    @Query("DELETE FROM barnItemList_tbl WHERE itemId=:itemId")
    suspend fun deleteNameBarnItemList(itemId: Int)

    @Query("DELETE  FROM barnItemList_tbl WHERE Name=:name ")
    suspend fun deleteNameBarnItemList2(name:String)

    @Query("SELECT * FROM barnItemList_tbl")
    fun getNameBarnItemListList(): LiveData<List<NameBarnItemList>>


    @Query("SELECT * FROM barnItemList_tbl WHERE itemId=:itemId LIMIT 1")
    suspend fun getNameBarnItemList(itemId:Int):NameBarnItemList

    @Query("SELECT * FROM barnItemList_tbl WHERE name=:name LIMIT 1")
    suspend fun getNameBarnItemListFromName(name:String):NameBarnItemList


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNameBarnItemList(nameBarnItemList: NameBarnItemList)

    @Query("SELECT * from barnItemList_tbl")
    fun getList(): Flow<List<NameBarnItemList>>



}
