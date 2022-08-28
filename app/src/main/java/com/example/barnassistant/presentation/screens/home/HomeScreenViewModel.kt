package com.example.barnassistant.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barnassistant.data.BarnListMapper
import com.example.barnassistant.data.DataOrException
import com.example.barnassistant.data.room.RoomRepositoryImpl
import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.NameBarnItemList
import com.example.barnassistant.domain.repository.FireRepository
import com.example.barnassistant.domain.repository.RoomRepository
import com.example.barnassistant.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: RoomRepositoryImpl,
    private  val utils: Utils
): ViewModel() {
    var data: MutableState<DataOrException<List<NameBarnItemList>, Boolean, Exception>> =
        mutableStateOf(DataOrException(listOf(), true, Exception("")))
    val listBarn = repository.getFavorites()
    val listName = repository.getList()
   private  val _nameList = MutableStateFlow<List<NameBarnItemList>>(emptyList())
    val barnItemNameList = MutableStateFlow(NameBarnItemList())
    init {
      getAllBooksFromDatabase()

        viewModelScope.launch(Dispatchers.IO) {
//            repository.getList().distinctUntilChanged()
//                .collect {
//                        listOfFavs ->
//                    if(listOfFavs.isNullOrEmpty()){
//                        Log.d("test", ": is empty ")
//                    }else{
//
//                        _nameList.value = listOfFavs
//                        Log.d("test", " list of names: ${_nameList.value} ")
//                    }
//                }

        }
    }

    private val time = mutableStateOf("")
    fun getTime(): String {
        time.value = utils.getCurrentTime()
        return time.value


    }


    private fun getAllBooksFromDatabase() {

        viewModelScope.launch {
            repository.getList().distinctUntilChanged()
                .collect {
                        listOfFavs ->
                    if(listOfFavs.isNullOrEmpty()){
                        Log.d("test", ": is empty ")
                    }else{

                        _nameList.value = listOfFavs
                        Log.d("test", " list of names: ${_nameList.value} ")
                    }
                }
            data.value.loading = true

          data.value.data= DataOrException(_nameList.value,true,null).data
          //  data.value.data = repository.getAllNameBarnItemListFromDatabase2().data
            if (!data.value.data.isNullOrEmpty()) data.value.loading = false
//        }
            Log.d("test", "getAllBooksFromDatabase: ${data.value.data?.toList().toString()}")

        }


    }
    fun getNameBarnItemList(nameBarnItemList: NameBarnItemList) {
        viewModelScope.launch {
            repository.getBarnItemName(nameBarnItemList)
        }
    }
        fun getNameBarnItemListFromName(name: String) {
           viewModelScope.launch {
            barnItemNameList.value=    repository.getBarnItemNameFromName(name)
               Log.d("test", "getNameBarnItemListFromName: ${barnItemNameList.value}")
            }


        }

        fun addNameBarnItemList(nameBarnItemList: NameBarnItemList) {
            viewModelScope.launch {
                repository.getCurrentTime()
                repository.addName(nameBarnItemList)

                Log.d(
                    "test",
                    "addNameBarnItemList: ${repository.getList().distinctUntilChanged().collect()}"
                )
            }
        }

        fun removeCard(nameBarnItemList: NameBarnItemList) {
            viewModelScope.launch {
                repository.deleteNameBarnItem(nameBarnItemList)
            }
        }

}