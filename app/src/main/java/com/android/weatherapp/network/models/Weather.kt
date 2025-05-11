package com.android.weatherapp.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val astronomy: List<Astronomy> = emptyList(),
    val avgtempC: String = "",
    val avgtempF: String = "",
    val date: String = "",
    val hourly: List<Hourly> = emptyList(),
    val maxtempC: String = "",
    val maxtempF: String = "",
    val mintempC: String = "",
    val mintempF: String = "",
    val sunHour: String = "",
    val totalSnow_cm: String = "",
    val uvIndex: String = ""
)