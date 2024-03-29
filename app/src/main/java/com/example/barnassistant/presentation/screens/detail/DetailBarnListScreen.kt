package com.example.barnassistant.presentation.screens.detail

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.barnassistant.R
import com.example.barnassistant.data.BarnListMapper
import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.BarnItemFB
import com.example.barnassistant.domain.model.NameBarnItemList
import com.example.barnassistant.presentation.components.BarnAppBar
import com.example.barnassistant.presentation.components.InputField
import com.example.barnassistant.presentation.components.LazyColumnBarnItemDB
import com.example.barnassistant.presentation.navigation.AppScreens
import com.example.barnassistant.presentation.screens.home.HomeScreenViewModel
import com.google.firebase.firestore.FirebaseFirestore

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun DetailBarnListScreen(
    navController: NavHostController,
    viewModel: BarnItemViewModel = hiltViewModel(),
    homeViewModel: HomeScreenViewModel= hiltViewModel(),
    curName:String=""
) {
    val sum= rememberSaveable { mutableStateOf(0f) }
    val listName = rememberSaveable { mutableStateOf(curName) }
    val name = rememberSaveable { mutableStateOf("") }
    val count = rememberSaveable { mutableStateOf("") }
    val price = rememberSaveable { mutableStateOf("") }
    val itemId = rememberSaveable { mutableStateOf(0) }
    val list = mutableListOf<BarnItem>()
    val myList=viewModel.favList.collectAsState().value
    val context = LocalContext.current
    Scaffold(
        topBar = {
            BarnAppBar(
                title ="Create new Barn list",
                icon = Icons.Default.ArrowBack,
                navController = navController,
                showProfile = false
            ) {

                navController.navigate(AppScreens.HomeScreen.name)
            }
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {
            Column {

                EditForm(
                    homeViewModel,
                    itemId,
                    listName,
                    name,
                    count,
                    price,
                    viewModel,
                    list = list,
                    modifier = Modifier
                        .height(500.dp)
                        .padding(16.dp)
                ) {
//                        searchQuery ->
//                    viewModel.searchBooks(query = searchQuery)

                }
                Spacer(modifier = Modifier.height(13.dp))

                val listBarnItemDB = viewModel._roomBarnList.collectAsState().value.filter {
                    it.listName == listName.value
                }

                LazyColumnBarnItemDB(
                    listBarnItemDB,
                    viewModel,
                    name,
                    count,
                    price,
                    itemId,
                    listName
                )
                sum.value=viewModel.getAmount(listBarnItemDB)
                Text(text = "${sum.value}")
            }
        }
    }

//    fun  saveToFirebase(
//        listData:List<BarnItemDB>,
//        data: BarnItemFB,
//        // navController: NavController,
//        context: Context,
//        isList: Boolean
//    ) {
//        val barnListMapper=BarnListMapper()
//        Log.d("save", "saveToFirebase: працює")
//        val list= mutableListOf<BarnItemFB>()
//        for(item in listData){
//            list.add(barnListMapper.mapBarnDBToBarnItemFB(item))
//        }
//        val db = FirebaseFirestore.getInstance()
//        val dbCollection = db.collection("books")
//        when  {
//            isList ->
//                for (i in list.indices)
//                // if (data.toString().isNotEmpty()) {
//
//                    dbCollection.add(list[i])
//                        .addOnSuccessListener { documentRef ->
//                            val docId = documentRef.id
//                            dbCollection.document(docId)
//                                .update(hashMapOf("id" to docId) as Map<String, Any>)
//                                .addOnCompleteListener { task ->
//                                    if (task.isSuccessful) {
//                                        // navController.popBackStack()
//                                        Toast.makeText(context,"saved", Toast.LENGTH_SHORT).show()
//                                    }
//                                }.addOnFailureListener {
//                                    Log.w("Error", "SaveToFirebase:  Error updating doc",it )
//                                }
//                        }
//            //   }else { Toast.makeText(context,"not saved", Toast.LENGTH_SHORT).show() }
//
//            !isList -> {
//
//                if (data.toString().isNotEmpty()) {
//                    dbCollection.add(data)
//                        .addOnSuccessListener { documentRef ->
//                            val docId = documentRef.id
//                            dbCollection.document(docId)
//                                .update(hashMapOf("id" to docId) as Map<String, Any>)
//                                .addOnCompleteListener { task ->
//                                    if (task.isSuccessful) {
//                                        // navController.popBackStack()
//                                        Toast.makeText(context,"saved", Toast.LENGTH_SHORT).show()
//                                        Log.d("save", "saveToFirebase: save")
//                                    }
//                                }.addOnFailureListener {
//                                    Log.w("Error", "SaveToFirebase:  Error updating doc",it )
//                                }
//                        }
//                }else { Toast.makeText(context,"not saved", Toast.LENGTH_SHORT).show() }
//            }
//            else -> {}
//        }
//    }
}

//@ExperimentalMate"rialApi
//@Composable
//private fun LazyColumnBarnItemDB(
//    listBarnItemDB: List<BarnItemDB>,
//    viewModel: BarnItemViewModel,
//    name: MutableState<String>,
//    count: MutableState<String>,
//    price: MutableState<String>,
//    itemId: MutableState<Int>,
//    listName: MutableState<String>
//) {
//    LazyColumn {
//        items(items = listBarnItemDB) { it ->
//
//            val barn = it
//            var unread by remember { mutableStateOf(false) }
//            val dismissState = rememberDismissState(
//                confirmStateChange = {
//                    if (it == DismissValue.DismissedToEnd) {
//                        unread = !unread
//                    }
//                    if (it == DismissValue.DismissedToStart) {
//                        viewModel.removeItem(barn)
//
//
//                    }
//                    //  it != DismissValue.DismissedToEnd
//                    false
//
//                }
//            )
//            SwipeToDismiss(
//                state = dismissState,
//                modifier = Modifier.padding(vertical = 4.dp),
//                directions = setOf(
//                    DismissDirection.StartToEnd,
//                    DismissDirection.EndToStart
//                ),
//                dismissThresholds = { direction ->
//                    FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
//                },
//                background = {
//                    val direction =
//                        dismissState.dismissDirection ?: return@SwipeToDismiss
//                    val color by animateColorAsState(
//                        when (dismissState.targetValue) {
//                            DismissValue.Default -> Color.Unspecified
//                            DismissValue.DismissedToEnd -> Color.Green
//                            DismissValue.DismissedToStart -> Color.Red
//                        }
//                    )
//                    val alignment = when (direction) {
//                        DismissDirection.StartToEnd -> Alignment.CenterStart
//                        DismissDirection.EndToStart -> Alignment.CenterEnd
//                    }
//                    val icon = when (direction) {
//                        DismissDirection.StartToEnd -> Icons.Default.Done
//                        DismissDirection.EndToStart -> {
//                            Icons.Default.Delete
//                        }
//
//
//                    }
//                    val scale by animateFloatAsState(
//                        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
//                    )
//
//                    Box(
//                        Modifier
//                            .fillMaxSize()
//                            .background(color)
//                            .padding(horizontal = 20.dp),
//                        contentAlignment = alignment
//                    ) {
//                        Icon(
//                            icon,
//                            contentDescription = "Localized description",
//                            modifier = Modifier.scale(scale)
//                        )
//                    }
//                },
//                dismissContent = {
//
//                    NoteRow(
//                        barnItem = BarnItemDB(
//                            name = it.name,
//                            count = it.count,
//                            price = it.price,
//                            itemId = it.itemId,
//                            listName = it.listName
//
//                        ),
//                        name = it.name,
//                        count = it.count.toString(),
//                        price = it.price.toString(),
//                        sum = viewModel.getItemSum(it),
//                        stringSum = when (it.count <= 1) {
//                            true -> ""
//                            else -> "sum :"
//                        }
//                    ) {
//                        name.value = it.name
//                        count.value = it.count.toString()
//                        price.value = it.price.toString()
//                        itemId.value = it.itemId
//                        listName.value = it.listName
//                        Log.d("test", "DetailBarnListScreen: $it ")
//
//                    }
//                })
//        }
//    }
//}


@ExperimentalComposeUiApi
@Composable
fun EditForm(
    homeViewModel: HomeScreenViewModel,
    itemId: MutableState<Int>,
    listName: MutableState<String>,
    name: MutableState<String>,
    count: MutableState<String>,
    price: MutableState<String>,
    viewModel: BarnItemViewModel,
    modifier: Modifier = Modifier,
    list: MutableList<BarnItem>,
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String) -> Unit = { }
) {
    val time= rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(listName.value, name.value, count.value, price.value) {
        listName.value.trim().isNotEmpty()
                && name.value.trim().isNotEmpty()
                && count.value.trim().isNotEmpty()
                && price.value.trim().isNotEmpty()
    }
    modifier
        .background(MaterialTheme.colors.background)
        .verticalScroll(rememberScrollState())

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isCreateAccount) Text(
            text = stringResource(R.string.create_acct),
            modifier = Modifier.padding(4.dp)
        ) else Text("")

        InputField(valueState = listName,
            labelId = "enter list name",
            keyboardType = KeyboardType.Text,
            enabled = when (list.size == 0) {
                true -> true
                else -> {
                    false
                }
            },
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions

                listName.value = ""
                keyboardController?.hide()
            })
        InputField(valueState = name,
            labelId = "enter  name",
            keyboardType = KeyboardType.Text,
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions

                name.value = ""
                keyboardController?.hide()
            })
        InputField(valueState = count,
            labelId = "enter count",
            keyboardType = KeyboardType.Number,
            enabled = true,

            onAction = KeyboardActions {

                if (!valid) return@KeyboardActions

                count.value = ""
                keyboardController?.hide()
            })
        InputField(valueState = price,
            labelId = "enter price",
            keyboardType = KeyboardType.Number,
            enabled = true,
            onAction = KeyboardActions {

                if (!valid) return@KeyboardActions

                price.value = ""
                keyboardController?.hide()
            })

        CreateButton(
           colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8D5ACC).copy(alpha = 0.7f)),
            textId = when (itemId.value == 0) {
                true -> "Create "
                else -> "Edit"
            },
            loading = loading,
            validInputs = valid
        ) {
            when (itemId.value == 0) {
                true -> {
                    list.add(
                        BarnItem(
                            name = name.value, count = count.value.toFloat(),
                            price = price.value.toFloat(), enabled = true, listName = listName.value,
                            isInFirebase = false
                        )
                    )
                    viewModel.addBarnItem(
                        name.value, count.value,
                        price.value, listName.value
                    )
                    viewModel.currentListName=listName.value
                    Log.d("test", "EditForm:befor  ${time.value}")
                    homeViewModel.getTime()
                    time.value=homeViewModel.time.value
                    Log.d("test", "EditForm: after ${time.value}")
                    if(!homeViewModel._nameList.value.contains(NameBarnItemList(name = listName.value,
                            createdTime = time.value))) {
                        homeViewModel.addNameBarnItemList(
                            NameBarnItemList(
                                name = listName.value,
                                createdTime = time.value
                            )
                        )
                        Log.d("test", "EditForm: $list")
                    }
                }
                else -> {
                    viewModel.editBarnItem(
                        inputName = name.value,
                        inputCount = count.value,
                        inputPrice = price.value,
                        inputListName = listName.value,
                        inputIteId = itemId.value
                    )
                    viewModel.currentListName=listName.value
                    Log.d("test", "EditForm: viewModel.barnItem  ${viewModel.barnItem.value}")
                }

            }

            onDone(listName.value.trim())
            price.value = ""
            count.value = ""
            name.value = ""
            itemId.value = 0
            keyboardController?.hide()
        }
    }
}

@Composable
fun CreateButton(
    colors: ButtonColors,
    modifier: Modifier=Modifier,
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    onClick: () -> Unit
) {
    Button(
        colors=colors,
        onClick = onClick,
        modifier = modifier
            .padding(3.dp)
            .fillMaxWidth(),
        enabled = validInputs,
        shape = CircleShape
    ) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = textId, modifier = Modifier.padding(5.dp))
    }
}

