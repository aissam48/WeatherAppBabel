package com.android.weatherapp.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val query: String = "",
    val type: String = ""
)