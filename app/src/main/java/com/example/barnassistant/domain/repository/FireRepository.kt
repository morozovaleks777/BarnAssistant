package com.example.barnassistant.domain.repository


import com.example.barnassistant.data.DataOrException
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.BarnItemFB
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(
    private val queryItem: Query
    ) {
    val dataOrException = DataOrException<List<BarnItemFB>, Boolean, Exception>()
    suspend fun getAllItemsFromDatabase(): DataOrException<List<BarnItemFB>, Boolean, Exception> {
        val dataOrException = DataOrException<List<BarnItemFB>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data =  queryItem.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(BarnItemFB::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false


        }catch (exception: FirebaseFirestoreException){
            dataOrException.e = exception
        }
        return dataOrException

    }




}