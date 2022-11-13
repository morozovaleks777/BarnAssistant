package com.example.barnassistant.presentation.screens.help

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.work.*
import com.example.barnassistant.DownloadWorker
import com.example.barnassistant.data.BarnListMapper
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.BarnItemFB
import com.example.barnassistant.domain.model.NameBarnItemList
import com.example.barnassistant.presentation.components.BarnAppBar
import com.example.barnassistant.presentation.screens.detail.BarnItemViewModel
import com.example.barnassistant.presentation.screens.home.HomeScreenViewModel
import com.example.barnassistant.utils.WorkerKeys
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun HelpScreen(navController: NavController) {


//    val downloadRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
//        .setConstraints(
//            Constraints.Builder()
//                .setRequiredNetworkType(
//                    NetworkType.CONNECTED
//                )
//                .build()
//        )
//        .build()
//    val colorFilterRequest = OneTimeWorkRequestBuilder<ColorFilterWorker>()
//        .build()
    val listFilterRequest=OneTimeWorkRequestBuilder<DownloadWorker>()
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(
                    NetworkType.CONNECTED
                )
                .build()
        )
        .build()
    val workManager = WorkManager.getInstance(LocalContext.current)
    Column(modifier = Modifier.fillMaxSize()) {
        BarnAppBar(
            title = "Help",
           icon = Icons.Filled.ArrowBack,
            onBackArrowClicked = { navController.popBackStack() },
            showProfile = false, onSearchClicked = {}, navController = navController
        )
        Column(
            modifier = Modifier.height(300.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Help.", style = MaterialTheme.typography.h4)


            val context= LocalContext.current
            val viewModel:BarnItemViewModel= hiltViewModel()
            val homeViewModel:HomeScreenViewModel= hiltViewModel()
            val myList=viewModel.favList.collectAsState().value
            Button(onClick = {
                saveToFirebase(homeViewModel,myList, data = BarnItemFB(count = 0f, enabled = false, listName = ""), context = context,isList = true)
            }) {
                Text(text = "start upload to cloud database")
            }
                    Spacer(modifier = Modifier.height(16.dp))
              //  }
               GoWork(workManager,

                   listFilterRequest)

            }
        }
        }

fun  saveToFirebase(
     homeViewModel:HomeScreenViewModel,
    listData:List<BarnItemDB>,
    data: BarnItemFB,
    // navController: NavController,
    context:Context,
    isList: Boolean
) {
    val barnListMapper= BarnListMapper()
    Log.d("save", "saveToFirebase: працює")
    val list= mutableListOf<BarnItemFB>()

    for(item in listData){
        item.time=homeViewModel._nameList.value.filter { listName ->listName.name==item.listName }[0].createdTime

        list.add(barnListMapper.mapBarnDBToBarnItemFB(item))
    }
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")
    when  {

        isList ->
            for (i in list.indices)
            // if (data.toString().isNotEmpty()) {
if(list[i].enabled==false && list[i].isInFirebase == false){
    list[i].isInFirebase=true
                dbCollection.add(list[i])
                    .addOnSuccessListener { documentRef ->
                        val docId = documentRef.id
                        dbCollection.document(docId)
                            .update(hashMapOf("id" to docId) as Map<String, Any>)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // navController.popBackStack()
                                    Toast.makeText(context,"saved", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.w("Error", "SaveToFirebase:  Error updating doc",it )
                            }
                    }}else{when{
    list[i].enabled==true ->Toast.makeText(context,"enabled must be false", Toast.LENGTH_SHORT).show()
    list[i].isInFirebase == true ->Toast.makeText(context,"already exist", Toast.LENGTH_SHORT).show()
                    }

                    }
        //   }else { Toast.makeText(context,"not saved", Toast.LENGTH_SHORT).show() }

        !isList -> {

            if (data.toString().isNotEmpty()) {
                dbCollection.add(data)
                    .addOnSuccessListener { documentRef ->
                        val docId = documentRef.id
                        dbCollection.document(docId)
                            .update(hashMapOf("id" to docId) as Map<String, Any>)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // navController.popBackStack()
                                    Toast.makeText(context,"saved", Toast.LENGTH_SHORT).show()
                                    Log.d("save", "saveToFirebase: save")
                                }
                            }.addOnFailureListener {
                                Log.w("Error", "SaveToFirebase:  Error updating doc",it )
                            }
                    }
            }else { Toast.makeText(context,"not saved", Toast.LENGTH_SHORT).show() }
        }
        else -> {}
    }
}

@Composable
 fun GoWork(
    workManager: WorkManager,

    listFilterRequest: OneTimeWorkRequest
) {
    val viewModel:BarnItemViewModel= hiltViewModel()

    val homeViewModel:HomeScreenViewModel= hiltViewModel()
    val workInfos = workManager
        .getWorkInfosForUniqueWorkLiveData("download")
        .observeAsState()
        .value

    val filterListInfo = remember(key1 = workInfos) {
        workInfos?.find { it.id == listFilterRequest.id }
    }

    val listString by derivedStateOf{
        val list=filterListInfo?.outputData?. getString(WorkerKeys.GET_LIST).toString()
        list
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        listString.let {
            val setList= mutableSetOf<NameBarnItemList>()
            val mapper = jacksonObjectMapper()
            val obj: List<BarnItemDB> = mapper.readValue(it)
            Log.d("list", "GoWork: list $obj")
            if(obj!=null){
            for(item in obj ){
               viewModel.saveFromFB(item)
                setList.add(NameBarnItemList(name = item.listName, createdTime = item.time, itemId = 0))
              //  homeViewModel.addNameBarnItemList(NameBarnItemList(name = item.listName, createdTime = item.time))

            }

                for(item in setList) {
                   val filteredList=homeViewModel._nameList.value.filter { nameBarnItemList ->nameBarnItemList.name==item.name  }
                    if(!homeViewModel._nameList.value.contains(item) && filteredList.isEmpty()){
                        homeViewModel.addNameBarnItemList(
                            NameBarnItemList(
                                name = item.name,
                                createdTime = item.createdTime
                            )
                        )
                    }

                }
            }
            Text(text =it )
        }
        Button(
            onClick = {
                workManager
                    .beginUniqueWork(
                        "download",
                        ExistingWorkPolicy.KEEP,

                            listFilterRequest
                    )


                    .enqueue()
            },
            enabled = filterListInfo?.state != WorkInfo.State.RUNNING
        ) {
            Text(text = "Start download")
        }

        Spacer(modifier = Modifier.height(8.dp))
        when (filterListInfo?.state) {
            WorkInfo.State.RUNNING -> Text("Downloading...")
            WorkInfo.State.SUCCEEDED -> Text("Download succeeded")
            WorkInfo.State.FAILED -> Text("Download failed")
            WorkInfo.State.CANCELLED -> Text("Download cancelled")
            WorkInfo.State.ENQUEUED -> Text("Download enqueued")
            WorkInfo.State.BLOCKED -> Text("Download blocked")
            else -> {}
        }
    }
}