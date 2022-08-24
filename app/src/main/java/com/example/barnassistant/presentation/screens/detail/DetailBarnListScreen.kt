package com.example.barnassistant.presentation.screens.detail

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.barnassistant.R
import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.presentation.components.BarnAppBar
import com.example.barnassistant.presentation.components.InputField
import com.example.barnassistant.presentation.components.NoteRow
import com.example.barnassistant.presentation.navigation.AppScreens

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun DetailBarnListScreen(
    navController: NavHostController,
    viewModel: BarnItemViewModel = hiltViewModel(),
) {
    val listName = rememberSaveable { mutableStateOf("") }
    val name = rememberSaveable { mutableStateOf("") }
    val count = rememberSaveable { mutableStateOf("") }
    val price = rememberSaveable { mutableStateOf("") }
    val itemId = rememberSaveable { mutableStateOf(0) }
    val list = mutableListOf<BarnItem>()
    Scaffold(
        topBar = {
            BarnAppBar(
                title = "Create new Barn list",
                icon = Icons.Default.ArrowBack,
                navController = navController,
                showProfile = false
            ) {

                navController.navigate(AppScreens.HomeScreen.name)
            }
        }
    ) {
        Surface {
            Column {
                EditForm(
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

                val listBarnItemDB = viewModel.favList.collectAsState().value.filter {
                    it.listName == listName.value
                }
                LazyColumn {
                    items(items = listBarnItemDB) { it ->

                        val barn = it
                        var unread by remember { mutableStateOf(false) }
                        val dismissState = rememberDismissState(
                            confirmStateChange = {
                                if (it == DismissValue.DismissedToEnd) {
                                    unread = !unread
                                }
                                if (it == DismissValue.DismissedToStart) {
                                    viewModel.removeItem(barn)


                                }
                                //  it != DismissValue.DismissedToEnd
                                false

                            }
                        )
                        SwipeToDismiss(
                            state = dismissState,
                            modifier = Modifier.padding(vertical = 4.dp),
                            directions = setOf(
                                DismissDirection.StartToEnd,
                                DismissDirection.EndToStart
                            ),
                            dismissThresholds = { direction ->
                                FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
                            },
                            background = {
                                val direction =
                                    dismissState.dismissDirection ?: return@SwipeToDismiss
                                val color by animateColorAsState(
                                    when (dismissState.targetValue) {
                                        DismissValue.Default -> Color.Unspecified
                                        DismissValue.DismissedToEnd -> Color.Green
                                        DismissValue.DismissedToStart -> Color.Red
                                    }
                                )
                                val alignment = when (direction) {
                                    DismissDirection.StartToEnd -> Alignment.CenterStart
                                    DismissDirection.EndToStart -> Alignment.CenterEnd
                                }
                                val icon = when (direction) {
                                    DismissDirection.StartToEnd -> Icons.Default.Done
                                    DismissDirection.EndToStart -> {
                                        Icons.Default.Delete
                                    }


                                }
                                val scale by animateFloatAsState(
                                    if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                                )

                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = alignment
                                ) {
                                    Icon(
                                        icon,
                                        contentDescription = "Localized description",
                                        modifier = Modifier.scale(scale)
                                    )
                                }
                            },
                            dismissContent = {

                                NoteRow(
                                    barnItem = BarnItemDB(
                                        name = it.name,
                                        count = it.count,
                                        price = it.price,
                                        itemId = it.itemId,
                                        listName = it.listName

                                    ),
                                    name = it.name,
                                    count = it.count.toString(),
                                    price = it.price.toString(),
                                    sum = viewModel.getItemSum(it),
                                    stringSum = when (it.count <= 1) {
                                        true -> ""
                                        else -> "sum :"
                                    }
                                ) {
                                    name.value = it.name
                                    count.value = it.count.toString()
                                    price.value = it.price.toString()
                                    itemId.value = it.itemId
                                    listName.value = it.listName
                                    Log.d("test", "DetailBarnListScreen: $it ")

                                }
                            })
                    }
                }
            }
        }
    }
}


@ExperimentalComposeUiApi
@Composable
fun EditForm(
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
                            price = price.value.toFloat(), enabled = true, listName = listName.value
                        )
                    )
                    viewModel.addBarnItem(
                        name.value, count.value,
                        price.value, listName.value
                    )
                    Log.d("test", "EditForm: $list")
                }
                else -> {
                    viewModel.editBarnItem(
                        inputName = name.value,
                        inputCount = count.value,
                        inputPrice = price.value,
                        inputListName = listName.value,
                        inputIteId = itemId.value
                    )

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
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        enabled = validInputs,
        shape = CircleShape
    ) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = textId, modifier = Modifier.padding(5.dp))
    }
}





