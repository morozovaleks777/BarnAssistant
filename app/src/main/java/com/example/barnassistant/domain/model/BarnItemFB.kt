package com.example.barnassistant.domain.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class BarnItemFB(
    @Exclude var id: String? = null,
    var itemId:Int?=null,
    val name:String?=null,
    var count: Float?=null,
    var price:Float?=null,
    val enabled:Boolean?=null,
    val listName:String?=null,
    val time:String?=null,
    var isInFirebase:Boolean?=null
){


    companion object{
        const val UNDEFINED_ID=0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BarnItem

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode() ?: 0
    }
}
