package com.example.models.models.appsettings

import android.content.Context
import androidx.datastore.dataStore
import com.example.models.models.Const.DATA_STORE_FILE_NAME
import com.example.models.models.appsettings.model.AppSettings
import com.example.models.models.appsettings.model.AppSettingsPrefs

class AppSettingsProtoDataStore(private val context: Context) : AppSettingsPrefs {

    private val Context.protoAppSettings by dataStore(DATA_STORE_FILE_NAME, AppSettingsSerializer)

    override suspend fun getSettings() = context.protoAppSettings.data

    override suspend fun putSettings(appSettings: AppSettings) {
        context.protoAppSettings.updateData {
            appSettings
        }
    }
}