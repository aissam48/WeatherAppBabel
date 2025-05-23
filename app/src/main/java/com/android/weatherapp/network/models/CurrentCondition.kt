package com.android.weatherapp.network.models

import kotlinx.serialization.Serializable

@Serializable
data class CurrentCondition(
    val FeelsLikeC: String = "",
    val FeelsLikeF: String = "",
    val cloudcover: String = "",
    val humidity: String = "",
    val observation_time: String = "",
    val precipInches: String = "",
    val precipMM: String = "",
    val pressure: String = "",
    val pressureInches: String = "",
    val temp_C: String = "",
    val temp_F: String = "",
    val uvIndex: String = "",
    val visibility: String = "",
    val visibilityMiles: String = "",
    val weatherCode: String = "",
    val weatherDesc: List<WeatherDesc> = emptyList(),
    val weatherIconUrl: List<WeatherIconUrl> = emptyList(),
    val winddir16Point: String = "",
    val winddirDegree: String = "",
    val windspeedKmph: String = "",
    val windspeedMiles: String = ""
)