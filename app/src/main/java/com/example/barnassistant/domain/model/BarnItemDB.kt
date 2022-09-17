package com.example.barnassistant.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "barn_tbl")
data class BarnItemDB (
    @ColumnInfo(name = "itemId")
    @PrimaryKey(autoGenerate = true)
    val itemId: Int,
    @ColumnInfo(name = "name")
    val name:String,
    @ColumnInfo(name = "count")
    val count:Float,
    @ColumnInfo(name = "price")
    val price:Float=0.0F,
    @ColumnInfo(name = "enabled")
    var enabled:Boolean=true,
    @ColumnInfo(name = "listName")
    val listName:String="",
    @ColumnInfo(name = "time")
    val time:String = ""

){
    companion object{
        const val UNDEFINED_ID=0
    }
}
