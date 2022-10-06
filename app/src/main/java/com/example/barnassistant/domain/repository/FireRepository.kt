package com.example.barnassistant.domain.repository


import com.example.barnassistant.data.DataOrException
import com.example.barnassistant.domain.model.BarnItemDB
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(
    private val queryBook: Query
    ) {
    suspend fun getAllBooksFromDatabase(): DataOrException<List<BarnItemDB>, Boolean, Exception> {
        val dataOrException = DataOrException<List<BarnItemDB>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data =  queryBook.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(BarnItemDB::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false


        }catch (exception: FirebaseFirestoreException){
            dataOrException.e = exception
        }
        return dataOrException

    }



}