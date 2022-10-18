package com.example.barnassistant.presentation.screens.counter

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.presentation.components.BarnAppBar
import com.example.barnassistant.presentation.components.FilterCard
import com.example.barnassistant.presentation.components.NoteRow
import com.example.barnassistant.presentation.screens.home.HomeScreenViewModel
import java.util.*


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun Counter(
    navController: NavController,
    viewModel: CounterViewModel = hiltViewModel(),
    homeViewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val listBarnItemDB = viewModel.favList.collectAsState().value.filter { barnItemDB ->
        !barnItemDB.enabled
    }
    val filteredListItemDb = rememberSaveable {
        mutableStateOf(listOf<BarnItemDB>())
    }
    val lisatOfFilters =
        listOf("All list", "name of list", "all sum", "sum per period", "item sum")
    var expanded by remember { mutableStateOf(false) }

    val currentFilteredList = viewModel.currentFilteredList.collectAsState().value
    val newList = rememberSaveable { currentFilteredList }
    val deletedItemList = remember {
        mutableStateListOf("All list", "name of list", "all sum", "sum per period", "item sum")
    }
    val isShowCalendar = rememberSaveable { mutableStateOf(false) }
    val isShowInputField = rememberSaveable { mutableStateOf(false) }
    val mDateBegin = remember { mutableStateOf("00.00.0000") }
    val mDateLast = remember { mutableStateOf("00.00.0000") }
    val enteredName = rememberSaveable {
        mutableStateOf("")
    }
    val sum = rememberSaveable { mutableStateOf(0f) }
    val listName = rememberSaveable { mutableStateOf("curName") }
    val name = rememberSaveable { mutableStateOf("") }
    val count = rememberSaveable { mutableStateOf("") }
    val price = rememberSaveable { mutableStateOf("") }
    val itemId = rememberSaveable { mutableStateOf(0) }
    Column(modifier = Modifier.fillMaxSize()) {
        BarnAppBar(
            title = "Counter",
            icon = Icons.Filled.ArrowBack,
            onBackArrowClicked = { navController.popBackStack() },
            showProfile = false, onSearchClicked = {}, navController = navController
        )
        Column(

            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Box(modifier = Modifier.height(50.dp)) {
                Column(Modifier.fillMaxWidth()) {
                    Button(
                        elevation = ButtonDefaults.elevation((-0).dp),
                        onClick = { expanded = true },

                        colors = ButtonDefaults
                            .buttonColors(
                                backgroundColor =
                                Color(0xFFDE6DF1).copy(alpha = 0.8f),
                            )

                    ) {
                        Text(
                            modifier = Modifier.align(alignment = Alignment.CenterVertically),
                            text = "choose filters", fontSize = 15.sp,
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background
                            (color = Color(0xFFDE6DF1).copy(alpha = 0.8f)),
                        offset = DpOffset(20.dp, 10.dp),
                        properties = PopupProperties(dismissOnBackPress = false)
                    ) {
                        for (item in lisatOfFilters) {
                            DropdownMenuItem(onClick = {
                                if (deletedItemList.contains(item)) deletedItemList.remove(item)
                                //  else

                                viewModel.currentFilteredList.value.add(
                                    item
                                )

                            }) {
                                Text(text = item)
                            }
                        }
                        Divider()
                        DropdownMenuItem(onClick = { }) {
                            Text(text = "Settings")
                        }
                    }
                }


            }
            val map = rememberSaveable {
                mutableStateOf(mutableMapOf<String, Float>())
            }

            LazyRow {
                itemsIndexed(items = newList,
                    itemContent = { _, it ->
                        AnimatedVisibility(
                            visible = !deletedItemList.contains(it),
                            enter = expandHorizontally(),
                            exit = shrinkHorizontally(animationSpec = tween(durationMillis = 1000))
                        )
                        {
                            FilterCard(filterName = it) {

                                deletedItemList.add(it)
                                if (deletedItemList.contains("sum per period")) {
                                    isShowCalendar.value = false
                                }
                                if (deletedItemList.contains("item sum")) {
                                    isShowInputField.value = false
                                }

                                if (viewModel.currentFilteredList.value.contains(it)) {
                                    viewModel.currentFilteredList.value.remove(
                                        it
                                    )
                                }
                                Log.d(
                                    "Counter",
                                    "Counter: viewModel.currentFilteredList.value ${viewModel.currentFilteredList.value}"
                                )

                            }
                        }
                    })


            }
            if (isShowCalendar.value) {
                MyCalendar(mDateBegin, mDateLast)
            } else Box() {}
            if (isShowInputField.value) {
                val valState = remember {
                    mutableStateOf("")
                }
                val keyboardController = LocalSoftwareKeyboardController.current
                com.example.barnassistant.presentation.components.InputField(valueState = valState,
                    labelId = "enter name",
                    keyboardType = KeyboardType.Text,
                    enabled = true,
                    onAction = KeyboardActions {


                        enteredName.value = valState.value
                        valState.value = ""
                        keyboardController?.hide()
                    })
            }
            Column {
                Log.d("Counter", "Counter:  currentFilteredList mDateB ${mDateBegin.value} ")
                Log.d("Counter", "Counter:  currentFilteredList mDataL ${mDateLast.value} ")
                //  sum.value = viewModel.getAllListWithSum(listBarnItemDB)
//                val map = rememberSaveable {
//                    mutableStateOf(mutableMapOf<String, Float>())
//                }
                when {

                    !deletedItemList.contains("All list") -> {
                        Log.d("Counter", "Counter: listBarnItemDB $listBarnItemDB ")
                        filteredListItemDb.value =
                            listBarnItemDB.filter { barnItemDB -> !barnItemDB.enabled }
                        val data = viewModel.getAllListWithSum(filteredListItemDb.value)
//                        if (map.value.isNotEmpty()) {
//                            map.value = mutableMapOf()
//                        }
                        map.value.put("All list", data)
                    }

                    !deletedItemList.contains("name of list") -> {
                   isShowInputField.value=true
                        filteredListItemDb.value =
                            listBarnItemDB.filter { barnItemDB -> barnItemDB.listName == enteredName.value }
                        val data = viewModel.getAllListWithSum(filteredListItemDb.value)
                        if(data!=0f){
                        map.value.put(enteredName.value, data)}
                    }

                    currentFilteredList.contains("all sum") -> {}

                    !deletedItemList.contains("sum per period") -> {
                        isShowCalendar.value = true
                        homeViewModel.getAllBooksFromDatabase()
                        val l =
                            viewModel.getSumFOrPeriod(
                                homeViewModel._nameList.collectAsState(listOf()).value,
                                mDateBegin.value, mDateLast.value
                            )
                        Log.d("Counter", "Counter: l $l ")
                        val l2 = mutableSetOf<String>()
                        for (i in l) {
                            l2.add(i.name)
                        }

                        filteredListItemDb.value =
                            listBarnItemDB.filter { barnItemDB -> l2.contains(barnItemDB.listName) }
                        val data = viewModel.getAllListWithSum(filteredListItemDb.value)
//                        if (map.value.isNotEmpty()) {
//                            map.value = mutableMapOf()
//                        }
                        if (mDateBegin.value != "00.00.0000" && mDateLast.value != "00.00.0000") {
                            map.value.put(
                                "sum from ${mDateBegin.value} till ${mDateLast.value}",
                                data
                            )
                        }
                        Log.d("Counter", "Counter:  map.value ${map.value}")
                    }
                    currentFilteredList.contains("item sum") -> {
                        isShowInputField.value = true
                        filteredListItemDb.value = listBarnItemDB.filter { barnItemDB ->
                            barnItemDB.name == enteredName.value
                        }
                        val data = viewModel.getAllListWithSum(filteredListItemDb.value)
                        if(data!=0.0f){
                            map.value.put("${enteredName.value} sum", data)
                        }

                    }
                }
                //   else -> {}
            }

            //  if (currentFilteredList.size != 0) {
//                    for (i in lisatOfFilters) {
//                        if (map.value.keys.contains(i))
//                            Text(text = "$i : ${map.value.get(i)}")
//                    }
//                if (map.value.contains("sum from ${mDateBegin.value} till ${mDateLast.value}")){
//                    val value="sum from ${mDateBegin.value} till ${mDateLast.value}"
//                    Text(text = "$value : ${map.value.get(value)}")
//                }
            for (key in map.value.keys) {
                Text(text = "$key : ${map.value.get(key)}")
            }
            //  }
            LazyColumn {
                items(items = filteredListItemDb.value) {
                    NoteRow(
                        onNoteClicked = {

                        },
                        name = it.name,
                        count = it.count.toString(),
                        price = it.price.toString(),
                        listName = it.listName,
                    )
                }
            }


        }


    }

}


@Composable
fun MyCalendar(a: MutableState<String>, b: MutableState<String>) {
    val mContext = LocalContext.current

    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    // Declaring a string value to
    // store date in string format
    val mDate = remember { mutableStateOf("") }
    val isFirst = remember { mutableStateOf(true) }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            a.value = "$mDayOfMonth.${mMonth + 1}.$mYear"
        }, mYear, mMonth, mDay
    )
    val mDatePickerDialog2 = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            b.value = "$mDayOfMonth.${mMonth + 1}.$mYear"
        }, mYear, mMonth, mDay
    )

    Column(
        modifier = Modifier.height(150.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Creating a button that on
        // click displays/shows the DatePickerDialog

        Button(onClick = {

            when (isFirst.value) {
                true -> {

                    a.value = "00.00.0000"
                    b.value = "00.00.0000"
                    mDatePickerDialog.show()
                    isFirst.value = !isFirst.value
                }
                else -> {
                    b.value = "00.00.0000"
                    mDatePickerDialog2.show()
                    isFirst.value = !isFirst.value
                }

            }
            // mDatePickerDialog.show()
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58))) {
            var text = ""
            text = when (isFirst.value) {
                true -> {
                    "enter first date"

                }
                else -> {
                    "enter last date"

                }
            }
            Text(text = text, color = Color.White)
        }

        // Adding a space of 100dp height
        Spacer(modifier = Modifier.size(5.dp))

        // Displaying the mDate value in the Text
        Text(text = " first Date: ${a.value}", fontSize = 30.sp, textAlign = TextAlign.Center)
        Text(text = " last Date: ${b.value}", fontSize = 30.sp, textAlign = TextAlign.Center)
    }
}


