package com.example.barnassistant.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.NameBarnItemList

//@Database(entities = [BarnItemDB::class],version = 1,exportSchema = false)
//abstract class AppRoomDatabase:RoomDatabase() {
//
//    abstract fun barnListDao():RoomDao
//
//    companion object{
//        private var INSTANCE:AppRoomDatabase?=null
//        private val LOCK=Any()
//        private const val DB_NAME="barn_item.db"
//        fun getInstance(application: Application):AppRoomDatabase{
//            INSTANCE?.let {
//                return it
//            }
//            synchronized(LOCK){
//                INSTANCE?.let {
//                    return it
//                }
//            }
//            val db= Room.databaseBuilder(
//                application,
//                AppRoomDatabase::class.java,
//                DB_NAME)
//                .build()
//            INSTANCE=db
//            return db
//        }
//    }
//}

@Database(entities = [BarnItemDB::class,NameBarnItemList::class],version = 10,exportSchema = false)
abstract class AppRoomDatabase:RoomDatabase() {
    abstract fun barnListDao():RoomDao
}