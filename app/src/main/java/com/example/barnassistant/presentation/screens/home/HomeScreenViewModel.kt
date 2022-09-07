package com.example.barnassistant.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barnassistant.data.BarnListMapper
import com.example.barnassistant.data.DataOrException
import com.example.barnassistant.data.room.RoomRepositoryImpl
import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.NameBarnItemList
import com.example.barnassistant.domain.repository.RoomRepository
import com.example.barnassistant.domain.useCases.GetBarnListUseCase
import com.example.barnassistant.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: RoomRepository,
    private val getBarnListUseCase: GetBarnListUseCase,
    private  val utils: Utils,
    val mapper: BarnListMapper
): ViewModel() {
    var data: MutableState<DataOrException<List<NameBarnItemList>, Boolean, Exception>> =
        mutableStateOf(DataOrException(listOf(), true, Exception("")))

   val listBarn =getBarnListUseCase.getBarnList()
    val listName = repository.getList()
    val _nameList = MutableStateFlow<List<NameBarnItemList>>(listOf()) //emptyList()
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

    val time = mutableStateOf("")
    fun getTime() {
        time.value = utils.getCurrentTime()
         time.value
        Log.d("test", "getTime: ${time.value}")


    }


fun getAllBooksFromDatabase() {

        viewModelScope.launch {
            repository.getList().distinctUntilChanged()
                .collect {
                        listOfFavs ->
//                    if(listOfFavs.isNullOrEmpty()){
//                        Log.d("test", ": is empty ")
//                    }else{

                        _nameList.value = listOfFavs
                        Log.d("test", " list of names: ${_nameList.value} ")
                   // }
                }
            data.value.loading = true

          data.value.data= DataOrException(_nameList.value,true,null).data
          //  data.value.data = repository.getAllNameBarnItemListFromDatabase2().data
            if (!data.value.data.isNullOrEmpty()) data.value.loading = false
//        }
            Log.d("test", "getAllBooksFromDatabase: ${data.value.data?.toList().toString()}")

        }


    }
    fun getNameBarnItemList(nameBarnItemList:String) {
        viewModelScope.launch {
            repository.getBarnItemName(nameBarnItemList)
        }
    }
        fun getNameBarnItemListFromName(name: String) {
           viewModelScope.launch {
            barnItemNameList.value=    repository.getBarnItemNameFromName(name)
//barnItemNameList2.value=barnItemNameList.value

               Log.d("test", "getNameBarnItemListFromName: ${barnItemNameList.value}")
            }


        }

        fun addNameBarnItemList(nameBarnItemList: NameBarnItemList) {
            viewModelScope.launch {
                repository.getCurrentTime()
          //  getNameBarnItemListFromName(nameBarnItemList.name)
                if(!_nameList.value.isNullOrEmpty() && !_nameList.value.contains(nameBarnItemList)||barnItemNameList.value.name=="" )
                {
                    repository.addName(nameBarnItemList)
                    Log.d("test", "addNameBarnItemList: ${_nameList.value}")
                }

                Log.d(
                    "test",
                    "addNameBarnItemList: ${repository.getList().distinctUntilChanged().collect()}"
                )
            }
        }

        fun removeCard(nameBarnItemList: NameBarnItemList) {
            viewModelScope.launch {
                Log.d("test", "removeCard:before nameBarnItemList ${nameBarnItemList} ")
                if(nameBarnItemList.name=="") {repository.deleteNameBarnItem(nameBarnItemList)
                getAllBooksFromDatabase()}

                getAllBooksFromDatabase()
                delay(2500)
                if(!_nameList.value.isNullOrEmpty()){
                repository.deleteNameBarnItem(barnItemNameList.value)
                getAllBooksFromDatabase()}
            }

//getAllBooksFromDatabase()
            Log.d("test", "removeCard: listName3 ${_nameList.value} ")

        }

}