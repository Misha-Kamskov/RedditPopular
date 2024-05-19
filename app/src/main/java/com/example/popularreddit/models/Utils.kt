package com.example.popularreddit.models

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.concurrent.TimeUnit

object Utils {
    fun getDifferenceInHours(unixTime: Double): Long {
        val givenCalendar = Calendar.getInstance().apply {
            timeInMillis = unixTime.toLong() * 1000
        }
        val currentCalendar = Calendar.getInstance()
        val diff = currentCalendar.timeInMillis - givenCalendar.timeInMillis
        return TimeUnit.MILLISECONDS.toHours(diff)
    }

    fun downloadImage(context: Context, imageUrl: String, imageName: String) {
        val request = DownloadManager.Request(Uri.parse(imageUrl))
            .setTitle("Downloading image")
            .setDescription("Downloading $imageName")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, imageName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

}
