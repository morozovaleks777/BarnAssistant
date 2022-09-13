package com.example.barnassistant.domain.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "barnItemList_tbl")
data class NameBarnItemList(
    @NonNull
    @ColumnInfo(name = "itemId")
    @PrimaryKey(autoGenerate = true)
    val itemId: Int= UNDEFINED_ID,
    var name:String = "",
    val createdTime:String =""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NameBarnItemList

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()

    }
companion object{
    const val UNDEFINED_ID= 0
}
}





