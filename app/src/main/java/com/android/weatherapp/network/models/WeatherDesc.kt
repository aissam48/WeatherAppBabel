package com.android.weatherapp.network.models

import kotlinx.serialization.Serializable

@Serializable
data class WeatherDesc(
    val value: String = ""
)