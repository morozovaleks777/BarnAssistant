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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.barnassistant.R
import com.example.barnassistant.domain.model.NameBarnItemList
import com.example.barnassistant.presentation.components.*
import com.example.barnassistant.presentation.navigation.AppScreens
import com.example.barnassistant.presentation.screens.channel_list_screen.ChannelViewModel
import com.example.barnassistant.presentation.screens.detail.BarnItemViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()


    Scaffold(
        scaffoldState = scaffoldState,
        drawerBackgroundColor = colorResource(id = R.color.purple_200),

        drawerContent = {
            Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
        },

        topBar = {
                       BarnAppBar(title = "Barn Assistant", navController = navController,
            onSearchClicked = {
                       isSearchIconClicked.value = (!isSearchIconClicked.value) }) },
            floatingActionButton = {
                Spacer(modifier = Modifier.padding(3.dp))
                        FABContent {
                                   navController.navigate(AppScreens.DetailScreen.name + "/${listName.value}") }
                                   },
        ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            HomeContent(scope,scaffoldState,
                navController,
                viewModel,
                detailViewModel,
                isSearchIconClicked = isSearchIconClicked.value)
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun HomeContent(
    scope:CoroutineScope,
    scaffoldState: ScaffoldState,
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
        Row(modifier = Modifier.align(alignment = Alignment.Start), verticalAlignment = Alignment.CenterVertically) {

            Icon(imageVector = Icons.Filled.Menu,
                contentDescription = "search Icon",
                modifier = Modifier
                    .scale(2f)
                    .padding(start = 20.dp)
                    .clickable {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
                , tint = Color(0xFF8D5ACC).copy(alpha = 0.7f))

            Spacer(modifier = Modifier.fillMaxWidth(0.6f))
            Column {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(AppScreens.ChannelListScreen.name)
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colors.secondaryVariant)
                Text(
                    text = currentUserName!!,
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.overline,
                    color = Color(0xFF223D02),
                    fontFamily = FontFamily.Cursive,
                    fontSize = 20.sp,
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
            ) { searchQuery -> filteredListOfNameBarnItemList.value =
                    listOfNameBarnItemList.filter {
                        it.name == searchQuery
                    }
            }
        }




        LastOpenedListArea(
            time = time.value,
            listOfBooks = filteredListOfNameBarnItemList.value,
            navController = navController, detailViewModel,
            homeViewModel = viewModel)

        TitleSection(label = "Last opened list",
            modifier = Modifier.heightIn(30.dp))

        LastOpenedListArea(
            time = time.value,
            listOfBooks = listOfNameBarnItemList,
            navController = navController, detailViewModel,
            homeViewModel = viewModel
        )
        TitleSection(label = "Previous  Lists", modifier = Modifier.heightIn(30.dp))
        AdvertView2()

        if (nameOfList != null) {
            OllListArea(

                nameOfList = nameOfList,
                time = nameOfList.createdTime,
                listOfNameBarnItemList = listOfNameBarnItemList,
                navController = navController,
                viewModel = detailViewModel,
                homeViewModel = viewModel)
        }
    }
}



@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable

fun HorizontalScrollableComponentAllList(
    navController: NavController,
    listOfBooks: List<NameBarnItemList>,
    viewModel: BarnItemViewModel = hiltViewModel(),
    channelViewModel: ChannelViewModel= hiltViewModel(),
    time: String = "",
    onLongPressed: (String) -> Unit,
    onCardPressed: (String) -> Unit,

    ) {
    val scrollState = rememberScrollState()
   // val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(180.dp, 230.dp)
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
                    }, onPressDetails = { onCardPressed(it) },
                    onDoubleClick = {

                        val filteredListBarnItemDb=viewModel._roomBarnList.value.filter { barnItemDB ->
                            barnItemDB.listName==book.name }
                        ChannelViewModel.filteredListBarnItemDB.value=filteredListBarnItemDb
ChannelViewModel.isNeedSendFile.value=true
          navController.navigate(AppScreens.ChannelListScreen.name)


                    }
                )
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
            .heightIn(max = 180.dp)
            .horizontalScroll(scrollState)
    ) {

        if (listOfListsName.isNullOrEmpty()) {
            Surface(modifier = Modifier.padding(10.dp)) {
                Text(

                    text = "No list found. Search a List",
                    modifier.heightIn(30.dp),
                    style = TextStyle(
                        color = Color.Red.copy(alpha = 0.4f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Cursive
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
           .heightIn(max=120.dp)
        , listOfBooks, time = time
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

    HorizontalScrollableComponentAllList(navController,listOfNameBarnItemList, time = time, onLongPressed = {
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
             // homeViewModel.getNameBarnItemListFromName(it)
            Log.d("test", "OllListArea: after $listOfNameBarnItemList ")
        }
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




