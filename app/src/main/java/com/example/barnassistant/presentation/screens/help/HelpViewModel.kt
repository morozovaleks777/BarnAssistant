package com.example.barnassistant.presentation.screens.help

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barnassistant.domain.useCases.GetItemListFromFBUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(
    val getItemListFromFBUseCase: GetItemListFromFBUseCase
):ViewModel() {
    fun getList(){
        viewModelScope.launch {
            getItemListFromFBUseCase.getBarnList()
        }

    }
}