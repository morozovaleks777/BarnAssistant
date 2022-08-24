package com.example.barnassistant.domain.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class BarnItemFB(
    @Exclude var id: String? = null,
    var itemId: Int = UNDEFINED_ID,
    @get:PropertyName("name")
    @set:PropertyName("name")
    var name:String="",
    var count: Float,
    var price:Float=0.0F,
    var enabled:Boolean,
    var listName:String
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
