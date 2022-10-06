package com.example.barnassistant.presentation.screens.channel_list_screen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import com.example.barnassistant.domain.model.BarnItemDB
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

@Singleton
class ChannelViewModel :ViewModel() {
    companion object{
        val filteredListBarnItemDB= MutableStateFlow(listOf<BarnItemDB>())
        val isNeedSendFile =
            mutableStateOf(true)

    }
}
