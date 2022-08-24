package com.example.barnassistant.data.room


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.barnassistant.data.BarnListMapper
import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.repository.RoomRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor (
    private val barnListDao: RoomDao): RoomRepository {


    private val mapper = BarnListMapper()

    override suspend fun deleteBarnItem(barnItem: BarnItem) {
        barnListDao.deleteBarnItem(mapper.mapBarnItemToBarnItemDB(barnItem).itemId)
    }

    override suspend fun editBarnItem(barnItem: BarnItemDB) {
        barnListDao.addBarnItem(barnItem)

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

    override fun getBarnList(): LiveData<List<BarnItem>> =
        Transformations.map(barnListDao.getBarnItemList()) {
            mapper.mapListBarnItemDBToListBarnItem(it)
        }
fun getFavorites(): Flow<List<BarnItemDB>> = barnListDao.getBarnList()

    override suspend fun addBarnItem(barnItem: BarnItem) {
        barnListDao.addBarnItem(mapper.mapBarnItemToBarnItemDB(barnItem))
        Log.d("test"," base ${getBarnList().value}")
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
}