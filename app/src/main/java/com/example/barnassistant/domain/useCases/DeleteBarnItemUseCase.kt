package com.example.barnassistant.domain.useCases

import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.repository.RoomRepository
import javax.inject.Inject

class DeleteBarnItemUseCase @Inject constructor(private  val barnListRepository: RoomRepository) {

   suspend fun deleteBarnItem(barnItem: BarnItem){
barnListRepository.deleteBarnItem(barnItem)
    }
}