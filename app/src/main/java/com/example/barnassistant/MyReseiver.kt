package com.example.barnassistant

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.os.Message
import android.util.Log
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.NameBarnItemList
import com.example.barnassistant.presentation.screens.detail.BarnItemViewModel
import com.example.barnassistant.presentation.screens.home.HomeScreenViewModel
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import javax.inject.Inject

class MyReseiver @Inject constructor(
val viewModel: BarnItemViewModel,
val homeViewModel: HomeScreenViewModel
): BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
       val action=intent?.action
        when(action){

            DownloadManager.ACTION_DOWNLOAD_COMPLETE ->{
                val f= File( "/sdcard/Download/","list.json")
                val list=     f.readText()
                f.deleteOnExit()
                Log.d("TAG", "onReceive: f.readText() ${f.readText()}")

viewModel.receivedList.value=list
                if (context != null) {
                    viewModel.newListReceived(context,list)
                }
                val mapper= jacksonObjectMapper()
                val obj: List<BarnItemDB> = mapper.readValue(list)
                homeViewModel.addNameBarnItemList(NameBarnItemList(name = obj[0].listName))
                Log.d("TAG", "onReceive: its worked  DownloadManager.ACTION_DOWNLOAD_COMPLETE  ")


            }

        }
    }
    companion object{
        const val ACTION_FILE_DOWNLOAD ="file download"
    }
}