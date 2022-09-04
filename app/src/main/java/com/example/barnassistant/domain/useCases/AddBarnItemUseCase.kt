package com.example.barnassistant.domain.useCases

import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.repository.RoomRepository
import javax.inject.Inject

class AddBarnItemUseCase @Inject constructor (private  val barnListRepository: RoomRepository){

  suspend  fun addBarnItem(barnItem: BarnItem){
      barnListRepository.addBarnItem(barnItem ) }
}