package com.example.barnassistant.presentation.screens.home
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barnassistant.data.BarnListMapper
import com.example.barnassistant.data.DataOrException
import com.example.barnassistant.domain.model.NameBarnItemList
import com.example.barnassistant.domain.repository.RoomRepository
import com.example.barnassistant.domain.useCases.GetBarnListUseCase
import com.example.barnassistant.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: RoomRepository,
    getBarnListUseCase: GetBarnListUseCase,
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
                    _nameList.value = listOfFavs
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
            delay(2500)
            getAllBooksFromDatabase()
            if(!_nameList.value.isNullOrEmpty() || barnItemNameList.value!=null|| _nameList.value.size!=1 ){

                getAllBooksFromDatabase()
            repository.deleteNameBarnItem(barnItemNameList.value)


              }
        }

        Log.d("test", "removeCard: listName3 ${_nameList.value} ")

    }

}