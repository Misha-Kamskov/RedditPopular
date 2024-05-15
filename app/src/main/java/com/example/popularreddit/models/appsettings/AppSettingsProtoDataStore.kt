package com.example.popularreddit.models.appsettings

import android.content.Context
import androidx.datastore.dataStore
import com.example.popularreddit.models.appsettings.model.AppSettings
import com.m.andrii.phonicsabc.models.appsettings.model.AppSettingsPrefs

class AppSettingsProtoDataStore(private val context: Context) : AppSettingsPrefs {

    private val Context.protoAppSettings by dataStore("appSettings.json", AppSettingsSerializer)

    override suspend fun getSettings() = context.protoAppSettings.data

    override suspend fun putSettings(appSettings: AppSettings) {
        context.protoAppSettings.updateData {
            appSettings
        }
    }
}