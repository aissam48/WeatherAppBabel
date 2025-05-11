package com.android.weatherapp.network.models.error_model

import kotlinx.serialization.Serializable

@Serializable
data class DataError(
    val error: List<Error>
)