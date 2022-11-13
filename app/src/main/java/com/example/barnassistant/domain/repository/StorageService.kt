package com.example.barnassistant.domain.repository

import com.example.barnassistant.domain.model.BarnItemFB

interface StorageService {

    fun addListener(
        userId: String,
        onDocumentEvent: (Boolean, BarnItemFB) -> Unit,
        onError: (Throwable) -> Unit
    )

    fun removeListener()
    fun getTask(taskId: String, onError: (Throwable) -> Unit, onSuccess: (BarnItemFB) -> Unit)
    fun saveTask(task: BarnItemFB, onResult: (Throwable?) -> Unit)
    fun updateTask(task: BarnItemFB, onResult: (Throwable?) -> Unit)
    fun deleteTask(taskId: String, onResult: (Throwable?) -> Unit)
    fun deleteAllForUser(userId: String, onResult: (Throwable?) -> Unit)

}