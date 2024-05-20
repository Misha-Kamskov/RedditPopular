package com.example.models.models.appsettings.model

import com.example.models.models.appsettings.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface AppSettingsPrefs {
    suspend fun getSettings(): Flow<AppSettings>
    suspend fun putSettings(appSettings: AppSettings)
}