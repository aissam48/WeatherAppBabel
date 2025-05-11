package com.android.weatherapp.network.models.error_model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorModel(
    val data: DataError
)