package com.example.barnassistant.domain.useCases

import com.example.barnassistant.domain.repository.RoomRepository
import javax.inject.Inject


class GetCurrentTimeCase @Inject constructor(private  val barnListRepository: RoomRepository) {
 suspend fun getCurrentTime():String {
    return barnListRepository.getCurrentTime()
    }
}