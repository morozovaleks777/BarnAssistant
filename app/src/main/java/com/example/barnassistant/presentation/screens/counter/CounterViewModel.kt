package com.example.barnassistant.presentation.screens.counter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.NameBarnItemList
import com.example.barnassistant.domain.useCases.GetAmountOfExpensesUseCase
import com.example.barnassistant.domain.useCases.GetBarnListUseCase
import com.example.barnassistant.domain.useCases.GetItemListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor(
    private val  getBarnListUseCase: GetBarnListUseCase,
    private val getItemListUseCase:GetItemListUseCase,
    private val getAmountOfExpensesUseCase: GetAmountOfExpensesUseCase,

):ViewModel() {

  //  val filterList= mutableListOf<String>("filter1","filter2","filter3","filter4","filter5","filter6","filter7")
    var currentFilteredList=MutableStateFlow(mutableListOf<String>())

    private val barnItemList = MutableStateFlow<List<BarnItemDB>>(listOf())
    val favList = barnItemList.asStateFlow()
var listik= mutableListOf<BarnItemDB>()
    init{
       getItemListDb()

        Log.d("CounterViewModel", "CounterViewModel barnItemList: ${listik}")
        getBarnItemList()
        Log.d("CounterViewModel", "CounterViewModel barnItemList from getBarnItemList() : ${favList.value}")
    }
 fun getItemListDb(){

    viewModelScope.launch(Dispatchers.IO) {
        getItemListUseCase.getBarnList().data?.distinctUntilChanged()?.collect(){
           list ->
          barnItemList.value=  list as MutableList<BarnItemDB>
listik= list as MutableList<BarnItemDB>
            Log.d("CounterViewModel", "getItemListDb: list $listik ")
               // barnItemList.value = list



        }
    }
}
    fun getBarnItemList() {
        viewModelScope.launch(Dispatchers.IO) {
            getBarnListUseCase.getBarnList().distinctUntilChanged()
                .collect() { listOfFavs ->
                    if (listOfFavs.isNullOrEmpty()) {
                        Log.d("test", ": is empty ")
                    } else {
                        barnItemList.value = listOfFavs

                    }
                }

        }

    }
    fun changeList(element:String){
//currentFilteredList.value=element

if(currentFilteredList.value.contains(element)){currentFilteredList.value.remove(element)}
        Log.d("Counter", "changeList: ${currentFilteredList.value.toList()} ")
    }

    fun getAllListWithSum(listOfItems:List<BarnItemDB>):Float {
       return getAmountOfExpensesUseCase.getAmount(listOfItems)
    }
    fun getSumFOrPeriod(list:List<NameBarnItemList>, beginDateString:String, lastDateString:String):List<NameBarnItemList> {


//        val beginDate = LocalDate.parse("19.09.2022", ofPattern("dd.MM.yyyy"))
//
//        val lastDate = LocalDate.parse("20.10.2022", DateTimeFormatter.ofPattern("dd.MM.yyyy"))

        val beginDate =if (lastDateString.isNotEmpty() && beginDateString.isNotEmpty()) {
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault(Locale.Category.FORMAT)).parse(
                beginDateString
            )?.time}else 0
        val lastDate =if (lastDateString.isNotEmpty() && beginDateString.isNotEmpty()) {
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault(Locale.Category.FORMAT)).parse(
                lastDateString
            )?.time}else 0


//return list.filter { nameBarnItemList -> LocalDate.parse(nameBarnItemList.createdTime,DateTimeFormatter.ofPattern("dd.MM.yyyy"))>localDate
//        &&  LocalDate.parse(nameBarnItemList.createdTime,DateTimeFormatter.ofPattern("dd.MM.yyyy"))<=localDate2 }

            return list.filter { nameBarnItemList ->

                ((SimpleDateFormat("dd.MM.yyyy", Locale.getDefault(Locale.Category.FORMAT)).parse(
                    nameBarnItemList.createdTime
                )?.time ?: 0) >= beginDate!!
                        && (SimpleDateFormat(
                    "dd.MM.yyyy",
                    Locale.getDefault(Locale.Category.FORMAT)
                ).parse(nameBarnItemList.createdTime)?.time
                    ?: 0) <= lastDate!!)
            }
        }
    }

