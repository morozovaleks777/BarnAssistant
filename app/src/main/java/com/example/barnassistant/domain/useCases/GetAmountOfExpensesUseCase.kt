package com.example.barnassistant.domain.useCases

import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.repository.RoomRepository
import javax.inject.Inject

class GetAmountOfExpensesUseCase @Inject constructor(private  val barnListRepository: RoomRepository) {

   fun getAmount(list: List<BarnItemDB>):Float {

      return  barnListRepository.getAmount(list)
    }
}