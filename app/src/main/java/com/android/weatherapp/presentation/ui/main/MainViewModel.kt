package com.android.weatherapp.presentation.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.weatherapp.network.ApiManager
import com.android.weatherapp.network.models.CityWeatherModel
import com.android.weatherapp.network.models.error_model.ErrorModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

sealed class WeatherUiState {
    data object Idle : WeatherUiState()
    data object Loading : WeatherUiState()
    data class Success(val data: List<CityWeatherModel>) : WeatherUiState()
    data class Error(val error: ErrorModel) : WeatherUiState()
}

class MainViewModel(private val apiManager: ApiManager) : ViewModel() {

    private val defaultCities = mutableListOf(
        "Casablanca, Morocco",
        "Rabat, Morocco",
        "Marrakech, Morocco",
        "Tangier, Morocco",
        "Fes, Morocco"
    )
    private val cities = mutableListOf<CityWeatherModel>()

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState

    private val _cityName = MutableStateFlow("")
    val cityName: StateFlow<String> = _cityName.asStateFlow()

    private val _searchByName = MutableStateFlow("")
    val searchByName: StateFlow<String> = _searchByName.asStateFlow()

    private val locationRequest = LocationRequest.create().apply {
        priority = Priority.PRIORITY_HIGH_ACCURACY
        interval = 5000
        fastestInterval = 2000
        numUpdates = 1
    }

    init {
        loadDefaultCities()
    }

    private fun loadDefaultCities() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            defaultCities.forEach { city ->
                apiManager.getCityWeather(city, 1, { success ->
                    cities.add(success)
                    _uiState.value = WeatherUiState.Success(data = cities.toList())
                }, { error ->
                    _uiState.value = WeatherUiState.Error(error)
                })
            }
        }
    }

    fun addCity(city: String) {
        viewModelScope.launch {
            apiManager.getCityWeather(city, 1, { success ->
                if (cities.isEmpty()){
                    cities.add(0,success)
                }else{
                    val checkCityIfExist = cities.find { it.data.request[0].query == success.data.request[0].query }
                    if (checkCityIfExist == null){
                        cities.add(0,success)
                    }
                }
                _uiState.value = WeatherUiState.Success(data = cities.toList())
            }, { error ->
                _uiState.value = WeatherUiState.Error(error)
            })

        }
    }

    fun search(city: String) {
        _searchByName.value = city
        val result = mutableListOf<CityWeatherModel>()
        result.addAll(cities.filter { it.data.request[0].query.lowercase().contains(city.lowercase()) })
        _uiState.value = WeatherUiState.Success(result)
    }

    fun removeCity(city: String) {
        viewModelScope.launch {
            cities.removeIf { it.data.request[0].query == city }
            _uiState.value = WeatherUiState.Success(data = cities.toList())
        }
    }

    fun refreshCities() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            val result = mutableListOf<CityWeatherModel>()
            cities.forEach { model ->
                apiManager.getCityWeather(model.data.request[0].query, 1, { success ->
                    result.add(success)
                }, { error ->
                    _uiState.value = WeatherUiState.Error(error)
                })

            }
            cities.clear()
            cities.addAll(result)
            _uiState.value = WeatherUiState.Success(data = cities.toList())

        }
    }

    fun setCityName(cityName: String) {
        _cityName.value = cityName
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun fetchCurrentLocation(context: Context, location: (location: Location?) -> Unit) {
        val fusedLocation = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            location(null)
            return
        }
        if (!isLocationEnabled(context)){
            enableGPS(context)
            return
        }
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation
                if (location != null) {
                    location(location)
                }
            }
        }

        fusedLocation.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun enableGPS(context: Context) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }

    fun getCityAndCountry(location: Location, context: Context): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        if (!addresses.isNullOrEmpty()) {
            val city = addresses[0].locality
            val country = addresses[0].countryName
            return "$city, $country"
        } else return null
    }
}