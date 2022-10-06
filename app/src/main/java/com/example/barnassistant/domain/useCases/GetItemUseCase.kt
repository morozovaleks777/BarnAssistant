package com.example.barnassistant.domain.useCases

import com.example.barnassistant.data.Resource
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.repository.RoomRepository
import javax.inject.Inject

class GetItemUseCase  @Inject constructor(private  val barnListRepository: RoomRepository) {
    suspend fun getBarnItem(itemId: Int): Resource<BarnItemDB> {
        return barnListRepository.getItem(itemId)
    }
}