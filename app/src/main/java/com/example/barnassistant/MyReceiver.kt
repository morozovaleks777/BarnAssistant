package com.example.barnassistant

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Environment.getExternalStorageDirectory
import android.util.Log
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.NameBarnItemList
import com.example.barnassistant.presentation.screens.detail.BarnItemViewModel
import com.example.barnassistant.presentation.screens.home.HomeScreenViewModel
import com.example.barnassistant.utils.Utils
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import javax.inject.Inject


class MyReceiver @Inject constructor(
    val viewModel: BarnItemViewModel,
    private val homeViewModel: HomeScreenViewModel,
    private val utils: Utils
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            DownloadManager.ACTION_DOWNLOAD_COMPLETE -> {

                val file = File("/sdcard/Download/", "list.json")
                val list = file.readText()
                    file.delete()
                viewModel.receivedList.value = list
                if (context != null) {
                    viewModel.newListReceived(context, list)
                }
                val mapper = jacksonObjectMapper()
                val obj: List<BarnItemDB> = mapper.readValue(list)
                homeViewModel.addNameBarnItemList(NameBarnItemList(name = obj[0].listName, createdTime =utils.getCurrentTime() ))
                Log.d("TAG", "onReceive: its worked  DownloadManager.ACTION_DOWNLOAD_COMPLETE  ")

            }
        }
    }
}