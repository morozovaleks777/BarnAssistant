package com.example.barnassistant.data.room


import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.barnassistant.data.BarnListMapper
import com.example.barnassistant.data.DataOrException
import com.example.barnassistant.data.Resource
import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.NameBarnItemList
import com.example.barnassistant.domain.repository.RoomRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor (
    private val barnListDao: RoomDao): RoomRepository{


    private val mapper = BarnListMapper()
    override suspend fun deleteBarnItemList(itemId: Int) {
        barnListDao.deleteBarnItem( itemId)
    }


    override suspend fun deleteBarnItem(barnItem: BarnItem) {
        barnListDao.deleteBarnItem(barnItem.itemId)
    }

    override suspend fun editBarnItem(barnItem: BarnItemDB) {
        barnListDao.addBarnItem(barnItem)
       // barnListDao.editBarnItem(barnItem)

    }

    override fun getAmount(list: List<BarnItem>): Float {
        var sum = 0F

        for (i in list.indices) {
            sum += list[i].count * list[i].price

        }
        return sum
    }

//    override suspend fun getBarnItem(itemId: Int): BarnItem {
//        val barnItemDB = barnListDao.getBarnItem(itemId)
//        return mapper.mapBarnDBToBarnItem(barnItemDB)
//    }
    override suspend fun getBarnItem(itemId: Int): BarnItemDB {
        val barnItemDB = barnListDao.getBarnItem(itemId)
        return barnItemDB
    }



    //    override fun getBarnList(): LiveData<List<BarnItemDB>> =barnListDao.getBarnItemList()
////        Transformations.map(barnListDao.getBarnItemList()) {
////            mapper.mapListBarnItemDBToListBarnItem(it)
////            barnListDao.getBarnItemList().value
////        }

    override  fun getBarnItemDBList(): Flow<List<BarnItemDB>> = barnListDao.getBarnList()

    override suspend fun addBarnItem(barnItem: BarnItem) {
        barnListDao.addBarnItem(mapper.mapBarnItemToBarnItemDB(barnItem))
    }


    override suspend  fun getCurrentTime():String{

        // Текущее время
        val currentDate = Date()

// Форматирование времени как "день.месяц.год"
        val dateFormat= SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val dateText: String = dateFormat.format(currentDate)

// Форматирование времени как "часы:минуты:секунды"
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val timeText: String = timeFormat.format(currentDate)
        while (true) {
            delay(1000)
            return "$timeText \n $dateText"
        }
    }



    override suspend fun  getItem(itemId: Int): Resource<BarnItemDB> {
        val response = try {
            Resource.Loading(data = true)

           barnListDao.getBarnItem(itemId)

        }catch (exception: Exception){

            return Resource.Error(message = "An error occurred ${exception.message.toString()}")
        }
        Resource.Loading(data = false)

        return Resource.Success(data = response)
    }

    override fun getItemDBList(): Resource<Flow<List<BarnItemDB>>> {
        val response = try {
            Resource.Loading(data = true)

            barnListDao.getBarnList()

        }catch (exception: Exception){

            return Resource.Error(message = "An error occurred ${exception.message.toString()}")
        }
        Resource.Loading(data = false)

        return Resource.Success(data = response)
    }


    override suspend fun addName(nameBarnItemList: NameBarnItemList) {
        barnListDao.addNameBarnItemList(nameBarnItemList)
    }


//    override suspend  fun< T> getAllNameBarnItemListFromDatabase(): DataOrException<List< T>, Boolean, Exception> {
//
//            val dataOrException = DataOrException<List<T>, Boolean, Exception>()
//
//            try {
//                dataOrException.loading = true
//                when(dataOrException){
//                  dataOrException as  DataOrException<List<NameBarnItemList>, Boolean, Exception> -> {
//                        dataOrException.data =  barnListDao.getNameBarnItemListList().value
//                        if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false
//                    }
//                }
//
//            }catch (exception: FirebaseFirestoreException){
//                dataOrException.e = exception
//            }
//            return dataOrException
//
//        }

   suspend  fun getAllNameBarnItemListFromDatabase2(): DataOrException<LiveData<List<NameBarnItemList>>, Boolean, Exception> {

        val dataOrException = DataOrException<LiveData<List<NameBarnItemList>>, Boolean, Exception>()

        try {
            dataOrException.loading = true

                    dataOrException.data =  barnListDao.getNameBarnItemListList()
                  //  if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false



        }catch (exception: FirebaseFirestoreException){
            dataOrException.e = exception
        }
        return dataOrException

    }

  override  fun getList():Flow<List<NameBarnItemList>> =barnListDao.getList()
//    override suspend fun getBarnItemName(nameBarnItemList: NameBarnItemList): NameBarnItemList {
//     return barnListDao.getNameBarnItemList(nameBarnItemList.itemId)
//    }

    override suspend fun getBarnItemName(name: String): NameBarnItemList {
        return barnListDao.getNameBarnItemListFromName(name)
    }

    override suspend fun getBarnItemNameFromName(name:String): NameBarnItemList {
        return barnListDao.getNameBarnItemListFromName(name)
    }

    override suspend fun deleteNameBarnItem(nameBarnItemList: NameBarnItemList) {
        barnListDao.deleteNameBarnItemList(nameBarnItemList.itemId)
    }

    override suspend fun deleteNameBarnItem2(nameBarnItemList: NameBarnItemList) {
       barnListDao.deleteNameBarnItemList2(nameBarnItemList.name)
    }


}
