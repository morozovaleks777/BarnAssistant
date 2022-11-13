package com.example.barnassistant.domain.model

data class BarnItem(
    var itemId: Int = UNDEFINED_ID,
    val name:String,
    val count: Float,
    val price:Float=0.0F,
    val enabled:Boolean,
    val listName:String,
    val time:String = "",
    val isInFirebase:Boolean=false
){
    companion object{
        const val UNDEFINED_ID=0
    }
}