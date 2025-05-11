package com.android.weatherapp.network.models.error_model

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val msg: String
)