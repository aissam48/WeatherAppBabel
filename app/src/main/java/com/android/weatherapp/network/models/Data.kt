package com.android.weatherapp.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val current_condition: List<CurrentCondition> = emptyList(),
    val request: List<Request> = emptyList(),
    val weather: List<Weather> = emptyList()
)