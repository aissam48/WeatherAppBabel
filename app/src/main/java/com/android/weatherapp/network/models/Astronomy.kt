package com.android.weatherapp.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Astronomy(
    val moon_illumination: String = "",
    val moon_phase: String = "",
    val moonrise: String = "",
    val moonset: String = "",
    val sunrise: String = "",
    val sunset: String = ""
)