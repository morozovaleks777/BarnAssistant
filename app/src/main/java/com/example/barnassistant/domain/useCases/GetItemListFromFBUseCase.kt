package com.example.barnassistant.domain.useCases

import com.example.barnassistant.data.DataOrException
import com.example.barnassistant.data.Resource
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.BarnItemFB
import com.example.barnassistant.domain.repository.FireRepository
import com.example.barnassistant.domain.repository.RoomRepository

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemListFromFBUseCase @Inject constructor(  val barnListRepository: FireRepository){
    suspend fun getBarnList(): DataOrException<List<BarnItemFB>, Boolean, Exception> {
        return  barnListRepository.getAllItemsFromDatabase()
    }
}