package com.example.barnassistant.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.NameBarnItemList
import com.example.barnassistant.presentation.components.BarnAppBar
import com.example.barnassistant.presentation.components.FABContent
import com.example.barnassistant.presentation.components.ListCard
import com.example.barnassistant.presentation.components.TitleSection
import com.example.barnassistant.presentation.navigation.AppScreens
import com.example.barnassistant.presentation.screens.detail.BarnItemViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController,
viewModel: HomeScreenViewModel = hiltViewModel(),
detailViewModel: BarnItemViewModel = hiltViewModel(),
) {
    Scaffold(topBar = {
        BarnAppBar(title = "Barn Assistant", navController = navController )
    },
        floatingActionButton = {
            FABContent{
                navController.navigate(AppScreens.DetailScreen.name)
            }

        }) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeContent(navController, viewModel,detailViewModel)
        }
    }
}

@Composable
fun HomeContent(navController: NavController, viewModel: HomeScreenViewModel,detailViewModel: BarnItemViewModel) {
  val listOfName :List<NameBarnItemList> =viewModel.listName.collectAsState(listOf()).value
  val listOfBarnItemDB:List<BarnItemDB> = viewModel.listBarn.collectAsState(listOf()).value

    //me @gmail.com
    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName = if (!email.isNullOrEmpty())
        FirebaseAuth.getInstance().currentUser?.email?.split("@")
            ?.get(0)else
        "N/A"
    Column(Modifier.padding(2.dp),
        verticalArrangement = Arrangement.Top) {
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
                Text(text = currentUserName!!,
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.overline,
                    color = Color.Red,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip)
                Divider()
            }


        }
        TitleSection(label = "Last opened list")
        LastOpenedListArea(listOfBooks = listOfName,
            navController =navController ,detailViewModel, homeViewModel = viewModel)
        TitleSection(label = "Previous  Lists")
        OllListArea(listOfBooks = listOfBarnItemDB,
            navController = navController)



    }

}

@Composable
fun OllListArea(listOfBooks: List<BarnItemDB>,
                navController: NavController) {
    val listOfListNames= mutableSetOf<String>()
    for(i in listOfBooks){
       listOfListNames.add(i.listName)
    }

    HorizontalScrollableComponentAllList(listOfListNames.toList()){
        navController.navigate(AppScreens.UpdateScreen.name +"/$it")
    }
}

@Composable
fun HorizontalScrollableComponentAllList(listOfBooks: List<String>,
                                         viewModel: HomeScreenViewModel = hiltViewModel(),
                                         time:String ="",
                                         onCardPressed: (String) -> Unit,

                                         ) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(280.dp)
            .horizontalScroll(scrollState)
    ) {

            if (listOfBooks.isNullOrEmpty()) {
                Surface(modifier = Modifier.padding(23.dp)) {
                    Text(
                        text = "No books found. Add a Book",
                        style = TextStyle(
                            color = Color.Red.copy(alpha = 0.4f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )

                }
            } else {

                for (book in listOfBooks) {
                    ListCard(book) {
                        //  onCardPressed(book.googleBookId.toString())

                    }
                }
            }
        }
    }

    @Composable

    fun HorizontalScrollableComponentLastList(
        listOfListsName: List<NameBarnItemList>,
        viewModel: HomeScreenViewModel = hiltViewModel(),
        time: String = "",
        onCardPressed: (String) -> Unit,

        ) {
        val scrollState = rememberScrollState()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(280.dp)
                .horizontalScroll(scrollState)
        ) {

                if (listOfListsName.isNullOrEmpty()) {
                    Surface(modifier = Modifier.padding(23.dp)) {
                        Text(
                            text = "No books found. Add a Book",
                            style = TextStyle(
                                color = Color.Red.copy(alpha = 0.4f),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )

                    }
                } else {


                    val card = listOfListsName[listOfListsName.lastIndex]
                    ListCard(card.name) {

                        onCardPressed(card.name)

                    }
                }

            }

        }



    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    fun LastOpenedListArea(
        listOfBooks: List<NameBarnItemList>,
        navController: NavController,
        viewModel: BarnItemViewModel,
        homeViewModel: HomeScreenViewModel
    ) {

        HorizontalScrollableComponentLastList(listOfBooks) {
            //navController.navigate(AppScreens.DetailScreen.name )
         homeViewModel.getNameBarnItemListFromName(it)
       homeViewModel.removeCard(homeViewModel.barnItemNameList.value)
        }

    }
