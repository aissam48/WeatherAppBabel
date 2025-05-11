package com.android.weatherapp.network

import com.android.weatherapp.network.models.CityWeatherModel
import com.android.weatherapp.network.models.error_model.DataError
import com.android.weatherapp.network.models.error_model.Error
import com.android.weatherapp.network.models.error_model.ErrorModel
import com.android.weatherapp.utils.Constant.API_KEY
import com.android.weatherapp.utils.Constant.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


class ApiManager() {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getCityWeather(
        name: String,
        numberOfDays: Int,
        onSuccess: (CityWeatherModel) -> Unit,
        onFailure: (ErrorModel) -> Unit
    ) {

        val response: HttpResponse =
            client.get(BASE_URL) {
                contentType(ContentType.Application.Json)
                parameter("q", name)
                parameter("format", "JSON")
                parameter("num_of_days", numberOfDays)
                parameter("key", API_KEY)
            }

        try {
            if (response.status.isSuccess()) {
                val responseBody: CityWeatherModel = response.body()
                onSuccess(responseBody)
            } else {
                val errorMessage: ErrorModel = response.body()
                onFailure(errorMessage)
            }
        } catch (e: Exception) {
            val errorMessage: ErrorModel = response.body()
            onFailure(errorMessage)
        }

    }

}
