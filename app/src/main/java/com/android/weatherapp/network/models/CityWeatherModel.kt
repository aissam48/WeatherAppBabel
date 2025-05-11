package com.android.weatherapp.network.models

import kotlinx.serialization.Serializable

@Serializable
data class CityWeatherModel(
    val data: Data = Data()
)