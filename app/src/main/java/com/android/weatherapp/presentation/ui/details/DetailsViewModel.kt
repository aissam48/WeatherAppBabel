package com.android.weatherapp.presentation.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.weatherapp.network.ApiManager
import com.android.weatherapp.network.models.CityWeatherModel
import com.android.weatherapp.network.models.Weather
import com.android.weatherapp.network.models.error_model.ErrorModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class ForecastUiState {
    data object Idle : ForecastUiState()
    data object Loading : ForecastUiState()
    data class Success(val data: CityWeatherModel) : ForecastUiState()
    data class Error(val error: ErrorModel) : ForecastUiState()
}

class DetailsViewModel(private val apiManager: ApiManager) : ViewModel() {

    private val _uiState = MutableStateFlow<ForecastUiState>(ForecastUiState.Idle)
    val uiState: StateFlow<ForecastUiState> = _uiState

    private val _selectedCity = MutableStateFlow<Weather?>(null)
    val selectedDay = _selectedCity.asStateFlow()

    fun getCityForecast(city: String) {
        _uiState.value = ForecastUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            apiManager.getCityWeather(city, 5, { success ->
                _selectedCity.value = success.data.weather[0]
                _uiState.value = ForecastUiState.Success(data = success)
            }, { error ->
                _uiState.value = ForecastUiState.Error(error = error)
            })
        }
    }

    fun refreshCity(city: String) {
        getCityForecast(city)
    }

    fun setDay(weather: Weather) {
        _selectedCity.value = weather
    }


}