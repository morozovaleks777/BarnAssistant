package com.example.barnassistant.presentation.screens.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barnassistant.data.BarnListMapper
import com.example.barnassistant.data.room.RoomRepositoryImpl
import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.useCases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarnItemViewModel @Inject constructor(
    val repository: RoomRepositoryImpl,
                                            private val editBarnItemUseCase: EditBarnItemUseCase,
                                            private val addBarnItemUseCase: AddBarnItemUseCase,
                                            private val getBarnItemUseCase: GetBarnItemUseCase,
                                            private val getBarnListUseCase: GetBarnListUseCase,
                                            private val deleteBarnItemUseCase: DeleteBarnItemUseCase,
                                            private val barnListMapper: BarnListMapper
): ViewModel() {





    private val _errorInputName=MutableLiveData<Boolean>()
            val errorInputName: LiveData<Boolean>
                get() = _errorInputName

    private val _errorInputCount=MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount


    val barnItem = MutableStateFlow<BarnItemDB>(BarnItemDB(name = "", count = 0f))


    private val _closeScreen=MutableLiveData<Unit>()
    val closeScreen: LiveData<Unit>
        get() = _closeScreen

fun getItemSum(barnItemDB: BarnItemDB):String{
    var sum=""
    if(barnItemDB.count>1f){
     sum=  ( barnItemDB.count*barnItemDB.price).toString()


    }
    return sum
}

    private val _roomBarnList = MutableStateFlow<List<BarnItemDB>>(emptyList())
    val favList = _roomBarnList.asStateFlow()
   init{

           viewModelScope.launch(Dispatchers.IO) {
               repository.getFavorites().distinctUntilChanged()
                   .collect {
                           listOfFavs ->
                       if(listOfFavs.isNullOrEmpty()){
                           Log.d("test", ": is empty ")
                       }else{
                           _roomBarnList.value = listOfFavs
                           Log.d("test", ": ${favList.value} ")
                       }
                   }

           }

}
    fun getBarnItem(barnItemId:Int){
        viewModelScope.launch {
            val item = getBarnItemUseCase.getBarnItem(barnItemId)
            barnItem.value=item
        }
    }
    fun removeItem(barnItemDB: BarnItemDB){
        viewModelScope.launch {
            deleteBarnItemUseCase.deleteBarnItem(barnListMapper.mapBarnDBToBarnItem(barnItemDB))

        }
    }

    fun addBarnItem(inputName: String?, inputCount: String?, inputPrice: String?, inputListName:String?) {
        val name = parseInputName(inputName)
        val count = parseInputCount(inputCount)
        val price = parseInputPrice(inputPrice )
        val listName = parseInputName(inputListName)
        val fieldsValid = validateInput(name, count,price)
//        if (fieldsValid) {
            viewModelScope.launch {
            val barnItem = BarnItem(name=name, count=count, price = price, enabled = true,listName= listName)

                addBarnItemUseCase.addBarnItem(barnItem)
            }
            finishWork()
       // }
    }

    fun editBarnItem(inputName:String?,inputCount:String?,inputPrice: String?,inputListName: String?,inputIteId:Int){
        val name=parseInputName(inputName)
        val listName=parseInputName(inputListName)
        val count=parseInputCount(inputCount)
        val price=parseInputPrice(inputPrice )
        val fieldsValid=validateInput(name,count,price)
//        if(fieldsValid){
           getBarnItem(inputIteId)
        Log.d("test", "EditForm: viewModel._barnItem  ${barnItem.value}")
          //  _barnItem.value?.let {
        barnItem.value.let {
            viewModelScope.launch {

                val item = it.copy(name = name, count = count,price = price, listName = listName)
                editBarnItemUseCase.editBarnItem(item)
                finishWork()
            }
        }
        }
   // }

   private fun parseInputName(inputName: String?):String{
         return inputName?.trim() ?: ""
}

    private fun parseInputCount(inputCount: String?):Float{
      return  try {
             inputCount?.trim()?.toFloat() ?:0f
        }
        catch (e:Exception){
            0f
        }
    }
    private fun parseInputPrice(inputPrice: String?):Float{
        return  try {
            inputPrice?.trim()?.toFloat() ?:0f
        }
        catch (e:Exception){
            0f
        }
    }

    private fun validateInput(inputName:String, inputCount: Float, inputPrice: Float):Boolean{
        var result=true
        if(inputName.isBlank()) {
           _errorInputName.value=true
             result = false
        }
        if(inputCount<=0){
            _errorInputCount.value=true
             result =false
        }
        if(inputPrice<=0){
            _errorInputCount.value=true
            result =false
        }
        return result
    }

    fun resetErrorInputName(){
        _errorInputName.value=false
    }
    fun resetErrorInputCount(){
        _errorInputCount.value=false
    }
    private fun finishWork(){
        _closeScreen.value=Unit
    }


}