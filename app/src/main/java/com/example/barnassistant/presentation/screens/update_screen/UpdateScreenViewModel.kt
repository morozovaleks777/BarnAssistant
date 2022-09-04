package com.example.barnassistant.presentation.screens.update_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barnassistant.data.room.RoomRepositoryImpl
import com.example.barnassistant.domain.model.NameBarnItemList
import com.example.barnassistant.domain.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateScreenViewModel@Inject constructor(
   val repository: RoomRepositoryImpl
):ViewModel() {
    val barnItemNameList = MutableStateFlow(NameBarnItemList())

    fun getNameBarnItemListFromName(name: String) {
        viewModelScope.launch {
            barnItemNameList.value=    repository.getBarnItemNameFromName(name)
            Log.d("test", " update getNameBarnItemListFromName: ${barnItemNameList.value}")
        }
}
}