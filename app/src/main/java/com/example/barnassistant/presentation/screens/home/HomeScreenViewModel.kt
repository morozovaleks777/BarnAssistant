package com.example.barnassistant.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barnassistant.data.BarnListMapper
import com.example.barnassistant.data.DataOrException
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.NameBarnItemList
import com.example.barnassistant.domain.repository.RoomRepository
import com.example.barnassistant.domain.useCases.GetBarnListUseCase
import com.example.barnassistant.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor (
    private val repository: RoomRepository,
    val getBarnListUseCase: GetBarnListUseCase,
    private  val utils: Utils,
    val mapper: BarnListMapper
): ViewModel() {
    var data: MutableState<DataOrException<List<NameBarnItemList>, Boolean, Exception>> =
        mutableStateOf(DataOrException(listOf(), true, Exception("")))

    val listBarn =MutableStateFlow<List<BarnItemDB>>(emptyList())
    val listName = repository.getList()
    val _nameList = MutableStateFlow<List<NameBarnItemList>>(listOf()) //emptyList()
    val barnItemNameList = MutableStateFlow(NameBarnItemList())


    init {

        Log.d("viewModel", ":${this.hashCode()} ")
        getAllBooksFromDatabase()
    }

    val time = mutableStateOf("")
    fun getTime() {
        time.value = utils.getCurrentTime()
        time.value
        Log.d("test", "getTime: ${time.value}")


    }

    fun getBarnItemList() {
        viewModelScope.launch(Dispatchers.IO) {
            getBarnListUseCase.getBarnList().distinctUntilChanged()
                .collect() { listOfFavs ->
                    if (listOfFavs.isNullOrEmpty()) {
                        Log.d("test", ": is empty ")
                    } else {
                       listBarn.value = listOfFavs

                    }
                }

        }
    }

    fun getAllBooksFromDatabase() {

        viewModelScope.launch {
            repository.getList().distinctUntilChanged()
                .collect {
                        listOfFavs ->
                val   newlistOfFavs=listOfFavs.toSet()

                    _nameList.value = newlistOfFavs .toList()
                    Log.d("test", " list of names: ${_nameList.value} ")
                }
            data.value.loading = true

            data.value.data= DataOrException(_nameList.value,true,null).data
            //  data.value.data = repository.getAllNameBarnItemListFromDatabase2().data
            if (!data.value.data.isNullOrEmpty()) data.value.loading = false

            Log.d("test", "getAllBooksFromDatabase: ${data.value.data?.toList().toString()}")

        }


    }

    fun getNameBarnItemListFromName(name: String) {
        viewModelScope.launch {
            if(!_nameList.value.isNullOrEmpty()){
                barnItemNameList.value=    repository.getBarnItemNameFromName(name)
            }
        }
    }

    fun addNameBarnItemList(nameBarnItemList: NameBarnItemList) {
        viewModelScope.launch {
            repository.getCurrentTime()
            val filterNameList=_nameList.value.filter {nameBarnItemList ->nameBarnItemList.name==nameBarnItemList.name   }
            if(!_nameList.value.isNullOrEmpty() && !_nameList.value.contains(nameBarnItemList)&&filterNameList.isEmpty()||barnItemNameList.value.name=="" )
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
            delay(2500)
            getAllBooksFromDatabase()
            if(!_nameList.value.isNullOrEmpty() || barnItemNameList.value!=null|| _nameList.value.size!=1 ){

               // getAllBooksFromDatabase()
                val filteredBarnList=listBarn.value.filter { barnItemDB -> barnItemDB.listName==nameBarnItemList.name }
            repository.deleteNameBarnItem(barnItemNameList.value)
                for(item in filteredBarnList){
                repository.deleteBarnItem(mapper.mapBarnDBToBarnItem(item))}


              }
        }

        Log.d("test", "removeCard: listName3 ${_nameList.value} ")

    }

}