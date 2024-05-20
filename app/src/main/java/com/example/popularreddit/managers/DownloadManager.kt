package com.example.popularreddit.managers

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

class DownloadManager {

    companion object {
        fun downloadMedia(context: Context, mediaUrl: String, mediaName: String) {
            val request = DownloadManager.Request(Uri.parse(mediaUrl))
                .setTitle("Downloading media")
                .setDescription("Downloading $mediaName")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mediaName)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        }
    }
}