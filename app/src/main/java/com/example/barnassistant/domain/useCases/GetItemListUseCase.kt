package com.example.barnassistant.domain.useCases

import com.example.barnassistant.data.Resource
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.repository.RoomRepository

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemListUseCase @Inject constructor(private  val barnListRepository: RoomRepository){
    fun getBarnList(): Resource<Flow<List<BarnItemDB>>> {
        return  barnListRepository.getItemDBList()
    }
}