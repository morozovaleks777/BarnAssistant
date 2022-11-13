package com.example.barnassistant

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.barnassistant.data.BarnListMapper
import com.example.barnassistant.domain.model.BarnItemDB
import com.example.barnassistant.domain.model.BarnItemFB
import com.example.barnassistant.domain.useCases.GetItemListFromFBUseCase
import com.example.barnassistant.utils.WorkerKeys
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.random.Random

@Suppress("BlockingMethodInNonBlockingContext")
@HiltWorker
class DownloadWorker @AssistedInject constructor(
val barnListMapper: BarnListMapper,
    private val getItemListFromFBUseCase: GetItemListFromFBUseCase,
    @Assisted val context: Context,
    @Assisted private val workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

val receivedListBarnItem= mutableListOf<BarnItemDB>()
    var str=""

    override suspend fun doWork(): Result {
        startForegroundService()
        delay(5000L)

       val response=getItemListFromFBUseCase.getBarnList()

        response.let { body ->
            return withContext(Dispatchers.IO) {
                val list=body.data
//                if(list==null ){
//                    delay(5000)}
                list?.forEach { item ->
                    receivedListBarnItem.add(barnListMapper.mapBarnFBToBarnItemDB(item))
                }
                    val mapper = jacksonObjectMapper()
                    val writer = mapper.writer(DefaultPrettyPrinter())
                    str = writer.writeValueAsString(receivedListBarnItem)



                Result.success(

                    workDataOf(
                        WorkerKeys.GET_LIST to str,


                        )

                )
            }
        }
    }

    private suspend fun startForegroundService() {
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(context, "download_channel")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentText("Downloading...")
                    .setContentTitle("Download in progress")
                    .build()
            )
        )
    }
}