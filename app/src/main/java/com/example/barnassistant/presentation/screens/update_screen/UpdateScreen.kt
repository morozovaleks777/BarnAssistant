package com.example.barnassistant.presentation.screens.update_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.barnassistant.domain.model.BarnItem
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.NameBarnItemList
import com.example.barnassistant.presentation.components.BarnAppBar
import com.example.barnassistant.presentation.components.FABContent
import com.example.barnassistant.presentation.components.LazyColumnBarnItemDB
import com.example.barnassistant.presentation.navigation.AppScreens
import com.example.barnassistant.presentation.screens.detail.BarnItemViewModel
import com.example.barnassistant.presentation.screens.home.HomeContent
import com.example.barnassistant.presentation.screens.home.HomeScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

@ExperimentalMaterialApi
@Composable
fun UpdateScreen(navController: NavController,
homeViewModel: HomeScreenViewModel,
detailHViewModel: BarnItemViewModel,
                 curName:String="",
updateScreenViewModel: UpdateScreenViewModel){
    val listName = rememberSaveable { mutableStateOf(curName) }
    val name = rememberSaveable { mutableStateOf("") }
    val count = rememberSaveable { mutableStateOf("") }
    val price = rememberSaveable { mutableStateOf("") }
    val itemId = rememberSaveable { mutableStateOf(0) }
    val sum= rememberSaveable { mutableStateOf(0f) }


    val listBarnItemDB = detailHViewModel.favList.collectAsState().value.filter {
       homeViewModel.getNameBarnItemListFromName(it.listName)

        it.listName == curName


    }


    Scaffold(  topBar = {

        BarnAppBar(
            title =curName,
            icon = Icons.Default.ArrowBack,
            navController = navController,
            showProfile = false
        ) {

            navController.navigate(AppScreens.HomeScreen.name)
        }
    },
        floatingActionButton = {
            FABContent{
                navController.navigate(AppScreens.DetailScreen.name+"/$curName")
            }

        }) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
          //  HomeContent(navController, viewModel,detailViewModel)
            Column() {
                LazyColumnBarnItemDB(
                    listBarnItemDB =listBarnItemDB ,
                    viewModel =detailHViewModel ,
                    name = name,
                    count = count,
                    price = price,
                    itemId =itemId ,
                    listName =listName
                )
                sum.value=       detailHViewModel.getAmount(listBarnItemDB)

                Text(text = "${sum.value}")
            }
        }
    }
}