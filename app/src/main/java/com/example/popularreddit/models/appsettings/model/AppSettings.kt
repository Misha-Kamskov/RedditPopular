package com.example.popularreddit.models.appsettings.model

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val infoBannerClosed: Boolean = false,
)
