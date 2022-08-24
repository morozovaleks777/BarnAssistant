package com.example.barnassistant.domain.useCases

import androidx.lifecycle.LiveData
import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.repository.RoomRepository
import javax.inject.Inject

class GetBarnListUseCase @Inject constructor(private  val barnListRepository: RoomRepository) {

    fun getBarnList():LiveData<List<BarnItem>>{
      return  barnListRepository.getBarnList()
    }


}