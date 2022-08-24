package com.example.barnassistant.domain.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class MBook(
                @Exclude var id: String? = null,
                @get:PropertyName("title")
                @set:PropertyName("title")
                 var title: String? = "",
                var authors:String? ="",
                 var notes: String? = "",
             ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MBook

        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        return title?.hashCode() ?: 0
    }
}
