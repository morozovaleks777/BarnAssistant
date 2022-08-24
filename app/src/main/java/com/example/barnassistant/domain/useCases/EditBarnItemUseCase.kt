package com.example.barnassistant.domain.useCases

import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.repository.RoomRepository
import javax.inject.Inject

class EditBarnItemUseCase @Inject constructor(private  val barnListRepository: RoomRepository) {

  suspend  fun editBarnItem(barnItem: BarnItemDB){
barnListRepository.editBarnItem(barnItem)
    }
}