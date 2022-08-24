package com.example.barnassistant.domain.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


//@Entity(tableName = "barn_tbl")
//data class BarnItemDB (
//    @ColumnInfo(name = "city")
//@PrimaryKey(autoGenerate = true)
//val itemId: Int,
//val name:String,
//val count:Float,
//val price:Float=0.0F,
//val enabled:Boolean,
//    @NonNull
//val listName:String
//
//)
@Entity(tableName = "barn_tbl")
data class BarnItemDB (
    @NonNull
    @ColumnInfo(name = "itemId")
    @PrimaryKey(autoGenerate = true)
    val itemId: Int=UNDEFINED_ID,
    val name:String,
    val count:Float,
    val price:Float=0.0F,
    var enabled:Boolean=true,
    val listName:String=""

){
    companion object{
        const val UNDEFINED_ID=0
    }
}
