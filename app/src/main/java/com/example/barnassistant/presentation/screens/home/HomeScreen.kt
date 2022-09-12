package com.example.barnassistant.presentation.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.barnassistant.domain.model.NameBarnItemList
import com.example.barnassistant.presentation.components.*
import com.example.barnassistant.presentation.navigation.AppScreens
import com.example.barnassistant.presentation.screens.detail.BarnItemViewModel
import com.google.firebase.auth.FirebaseAuth

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    detailViewModel: BarnItemViewModel = hiltViewModel(),
) {
    val listName = rememberSaveable { mutableStateOf(" new list") }
    val isSearchIconClicked = remember {
        mutableStateOf(false)
    }
    Scaffold(topBar = {
        BarnAppBar(title = "Barn Assistant", navController = navController,
            onSearchClicked = {
                isSearchIconClicked.value = (!isSearchIconClicked.value)
            })
    },
        floatingActionButton = {
            FABContent {
                navController.navigate(AppScreens.DetailScreen.name + "/${listName.value}")
            }

        }) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {

            HomeContent(
                navController,
                viewModel,
                detailViewModel,
                isSearchIconClicked = isSearchIconClicked.value
            )

        }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun HomeContent(
    navController: NavController,
    viewModel: HomeScreenViewModel,
    detailViewModel: BarnItemViewModel,
    isSearchIconClicked: Boolean
) {

    val listOfNameBarnItemList: List<NameBarnItemList> =
        viewModel._nameList.collectAsState(listOf()).value
    val filteredListOfNameBarnItemList =
        rememberSaveable { mutableStateOf(emptyList<NameBarnItemList>()) }
    val time = rememberSaveable { mutableStateOf("") }
    val nameOfList = viewModel.barnItemNameList.collectAsState(NameBarnItemList()).value


    //me @gmail.com
    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName = if (!email.isNullOrEmpty())
        FirebaseAuth.getInstance().currentUser?.email?.split("@")
            ?.get(0) else
        "N/A"
    Column(
        Modifier.padding(2.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(modifier = Modifier.align(alignment = Alignment.Start)) {
            //  TitleSection(label = "Your reading \n " + " activity right now...")
            Spacer(modifier = Modifier.fillMaxWidth(0.7f))
            Column {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(AppScreens.ReaderStatsScreen.name)
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colors.secondaryVariant)
                Text(
                    text = currentUserName!!,
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.overline,
                    color = Color.Red,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                Divider()
            }


        }

        if (isSearchIconClicked) {
            SearchForm(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) { searchQuery ->
                Log.d("test", "HomeContent: $searchQuery")
                Log.d("test", "HomeContent: $listOfNameBarnItemList")
                //  viewModel.searchBooks(query = searchQuery)
                filteredListOfNameBarnItemList.value =
                    listOfNameBarnItemList.filter {
                        it.name == searchQuery

                    }

                Log.d("test", "HomeContent: listOfBarnItemDB2 $filteredListOfNameBarnItemList")
            }
        }

        LastOpenedListArea(
            time = time.value, listOfBooks = filteredListOfNameBarnItemList.value,
            navController = navController, detailViewModel, homeViewModel = viewModel
        )

        TitleSection(label = "Last opened list")

        LastOpenedListArea(
            time = time.value, listOfBooks = listOfNameBarnItemList,
            navController = navController, detailViewModel, homeViewModel = viewModel
        )
        TitleSection(label = "Previous  Lists")

        if (nameOfList != null) {
            OllListArea(
                nameOfList = nameOfList,
                time = nameOfList.createdTime,
                listOfNameBarnItemList = listOfNameBarnItemList,
                navController = navController,
                viewModel = detailViewModel,
                homeViewModel = viewModel
            )
        }


    }

}


//@Composable
//fun HorizontalScrollableComponentAllList(listOfBooks: List<String>,
//                                         viewModel: HomeScreenViewModel = hiltViewModel(),
//                                         time:String ="",
//                                         onCardPressed: (String) -> Unit,
//
//                                         ) {
//    val scrollState = rememberScrollState()
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .heightIn(280.dp)
//            .horizontalScroll(scrollState)
//    ) {
//
//            if (listOfBooks.isNullOrEmpty()) {
//                Surface(modifier = Modifier.padding(23.dp)) {
//                    Text(
//                        text = "No list found. Add a List",
//                        style = TextStyle(
//                            color = Color.Red.copy(alpha = 0.4f),
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 14.sp
//                        )
//                    )
//
//                }
//            } else {
//
//                for (book in listOfBooks) {
//                    ListCard( modifier =Modifier.width(202.dp) ,book, time = time) {
//                      onCardPressed(it)
//
//                    }
//                }
//            }
//        }
//    }

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
//fun HorizontalScrollableComponentAllList(nameOfList: NameBarnItemList,listOfBooks: List<NameBarnItemList>,
fun HorizontalScrollableComponentAllList(
    listOfBooks: List<NameBarnItemList>,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    time: String = "",
    onLongPressed: (String) -> Unit,
    onCardPressed: (String) -> Unit,

    ) {

    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            // .fillMaxWidth()
            // .heightIn(280.dp)
            .horizontalScroll(scrollState)
    ) {

        if (listOfBooks.isNullOrEmpty() || listOfBooks[0].name == "") {
            Surface(modifier = Modifier.padding(23.dp)) {
                Text(
                    text = "No list found. Add a List",
                    style = TextStyle(
                        color = Color.Red.copy(alpha = 0.4f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )

            }
        } else {

            for (book in listOfBooks) {

                ListCard(modifier = Modifier.width(202.dp),
                    book = book.name, time = book.createdTime, onLongPressed = {


                        onLongPressed(it)
                        // viewModel.getNameBarnItemListFromName(it)
                    }, onPressDetails = { onCardPressed(it) })
            }
        }
    }
}


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable

fun HorizontalScrollableComponentLastList(
    modifier: Modifier = Modifier,
    listOfListsName: List<NameBarnItemList>,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    time: String = "",
    onCardPressed: (String) -> Unit


) {
    val scrollState = rememberScrollState()

    Row(
        modifier
            .fillMaxWidth()
            .heightIn(280.dp)
            .horizontalScroll(scrollState)
    ) {

        if (listOfListsName.isNullOrEmpty()) {
            Surface(modifier = Modifier.padding(23.dp)) {
                Text(
                    text = "No list found. Search a List",
                    style = TextStyle(
                        color = Color.Red.copy(alpha = 0.4f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )

            }
        } else {


            val card = listOfListsName[listOfListsName.lastIndex]

            ListCard(
                modifier = modifier,
                book = card.name,
                time = card.createdTime,
                onPressDetails = {

                    onCardPressed(card.name)

                })
        }

    }

}


@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun LastOpenedListArea(
    time: String,
    listOfBooks: List<NameBarnItemList>,
    navController: NavController,
    viewModel: BarnItemViewModel,
    homeViewModel: HomeScreenViewModel
) {

    HorizontalScrollableComponentLastList(
        modifier = Modifier
            .height(120.dp), listOfBooks, time = time
    ) {

        homeViewModel.getNameBarnItemListFromName(it)
        Log.d("test", "LastOpenedListArea viewModel.currentListName :${viewModel.currentListName} ")
        Log.d(
            "test",
            "LastOpenedListArea homeViewModel.barnItemNameList :${homeViewModel.barnItemNameList.value} "
        )
        navController.navigate(AppScreens.UpdateScreen.name + "/$it")

    }

}
//@Composable
//fun OllListArea(time:String,
//    listOfBooks: List<BarnItemDB>,
//                navController: NavController,
//                viewModel: BarnItemViewModel,
//                homeViewModel: HomeScreenViewModel) {
//    val listOfListNames= mutableSetOf<String>()
//    for(i in listOfBooks){
//        listOfListNames.add(i.listName)
//    }
//
//    HorizontalScrollableComponentAllList(listOfListNames.toList(), time = time){
//        homeViewModel.getNameBarnItemListFromName(it)
//        Log.d("test", "LastOpenedListArea  OllListArea viewModel.currentListName :${viewModel.currentListName} ")
//        Log.d("test", "LastOpenedListArea  OllListArea homeViewModel.barnItemNameList :${homeViewModel.barnItemNameList.value} ")
//
//
//      //  homeViewModel.barnItemNameList.value.name = it
//        navController.navigate(AppScreens.UpdateScreen.name +"/$it")
//    }
//}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun OllListArea(
    nameOfList: NameBarnItemList, time: String,
    listOfNameBarnItemList: List<NameBarnItemList>,
    navController: NavController,
    viewModel: BarnItemViewModel,
    homeViewModel: HomeScreenViewModel
) {

    val listBarnItemDB = viewModel.favList.collectAsState().value
    viewModel.getBarnItemList()


    // val listBarnItemDB = viewModel.favList. collectAsState(mutableListOf()).value
    HorizontalScrollableComponentAllList(listOfNameBarnItemList, time = time, onLongPressed = {
        homeViewModel.getNameBarnItemListFromName(it)
        Log.d("test", "OllListArea: $listOfNameBarnItemList ")
        if (!listOfNameBarnItemList.isNullOrEmpty()) {

            Log.d(
                "test",
                "OllListArea: homeViewModel.getNameBarnItemListFromName(it) ${homeViewModel.barnItemNameList.value.name} "
            )
            val curList = listBarnItemDB.filter { barnItemDB ->
                // barnItemDB.listName == homeViewModel.barnItemNameList.value.name
                barnItemDB.listName == it

            }
            Log.d("test1", "OllListArea: curlist $curList ")
            for (item in curList) {
                viewModel.removeItem(item)

            }
            Log.d("test", "OllListArea: after removing istBarnItemDB.filter  $listBarnItemDB")


            homeViewModel.removeCard(homeViewModel.barnItemNameList.value)
            //  homeViewModel.getNameBarnItemList(it)
            Log.d("test", "OllListArea: after $listOfNameBarnItemList ")
        }
//          homeViewModel.removeCard(homeViewModel.barnItemNameList.value)
//         //  homeViewModel.getNameBarnItemList(it)
//      Log.d("test", "OllListArea: after $listOfNameBarnItemList ")

    })
    {

        homeViewModel.getNameBarnItemListFromName(it)

        navController.navigate(AppScreens.UpdateScreen.name + "/$it")

    }

}

@ExperimentalComposeUiApi
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
) {
    Column {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()

        }

        InputField(
            valueState = searchQueryState,
            labelId = "Search",
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            },
            keyboardType = KeyboardType.Text
        )

    }


}



