package com.example.barnassistant.data

import com.example.barnassistant.domain.model.BarnItemFB
import com.example.barnassistant.domain.repository.StorageService
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class StorageServiceImpl @Inject constructor(): StorageService {
    private var listenerRegistration: ListenerRegistration? = null
    override fun addListener(
        userId: String,
        onDocumentEvent: (Boolean, BarnItemFB) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val query = Firebase.firestore.collection(TASK_COLLECTION).whereEqualTo(USER_ID, userId)

        listenerRegistration = query.addSnapshotListener { value, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            value?.documentChanges?.forEach {
                val wasDocumentDeleted =false //it.type == REMOVED
                val task = it.document.toObject(BarnItemFB::class.java).copy(id = it.document.id)
                onDocumentEvent(wasDocumentDeleted, task)
            }
        }
    }

    override fun removeListener() {
        listenerRegistration?.remove()
    }

    override fun getTask(
        taskId: String,
        onError: (Throwable) -> Unit,
        onSuccess: (BarnItemFB) -> Unit
    ) {
        Firebase.firestore
            .collection(TASK_COLLECTION)
            .document(taskId)
            .get()
            .addOnFailureListener { error -> onError(error) }
            .addOnSuccessListener { result ->
                val task = result.toObject(BarnItemFB::class.java)?.copy(id = result.id)
               // onSuccess(task ?: BarnItemFB())
            }
    }

    override fun saveTask(task: BarnItemFB, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(TASK_COLLECTION)
            .add(task)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun updateTask(task: BarnItemFB, onResult: (Throwable?) -> Unit) {
        task.id?.let {
            Firebase.firestore
                .collection(TASK_COLLECTION)
                .document(it)
                .set(task)
                .addOnCompleteListener { onResult(it.exception) }
        }
    }

    override fun deleteTask(taskId: String, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(TASK_COLLECTION)
            .document(taskId)
            .delete()
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun deleteAllForUser(userId: String, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(TASK_COLLECTION)
            .whereEqualTo(USER_ID, userId)
            .get()
            .addOnFailureListener { error -> onResult(error) }
            .addOnSuccessListener { result ->
                for (document in result) document.reference.delete()
                onResult(null)
            }
    }
    companion object {
        private const val TASK_COLLECTION = "Task"
        private const val USER_ID = "userId"
    }
}