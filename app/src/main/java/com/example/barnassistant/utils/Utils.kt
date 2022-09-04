package com.example.barnassistant.utils

import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class Utils @Inject constructor(){


     fun getCurrentTime():String{

        // Текущее время
        val currentDate = Date()

// Форматирование времени как "день.месяц.год"
        val dateFormat= SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val dateText: String = dateFormat.format(currentDate)

// Форматирование времени как "часы:минуты:секунды"
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val timeText: String = timeFormat.format(currentDate)
        while (true) {
          //  delay(1000)
            return " $dateText"
        }
    }
}