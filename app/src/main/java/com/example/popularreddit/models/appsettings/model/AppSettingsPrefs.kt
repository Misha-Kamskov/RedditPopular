package com.example.popularreddit.models.appsettings.model

import kotlinx.coroutines.flow.Flow

interface AppSettingsPrefs {
    suspend fun getSettings(): Flow<AppSettings>
    suspend fun putSettings(appSettings: AppSettings)
}