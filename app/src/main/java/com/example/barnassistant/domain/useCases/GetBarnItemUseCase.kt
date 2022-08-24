package com.example.barnassistant.domain.useCases

import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.repository.RoomRepository
import javax.inject.Inject

class GetBarnItemUseCase @Inject constructor(private  val barnListRepository: RoomRepository) {
   suspend fun getBarnItem(itemId: Int): BarnItemDB {
 return barnListRepository.getBarnItem(itemId)
    }
}