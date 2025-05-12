package com.android.weatherapp.presentation.ui.main

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.android.weatherapp.R
import com.android.weatherapp.network.models.CityWeatherModel
import com.android.weatherapp.presentation.navigation.NavRoutes
import com.android.weatherapp.presentation.ui.common_components.ErrorMessage
import com.android.weatherapp.presentation.ui.main.components.CityCard
import com.android.weatherapp.presentation.ui.main.components.EmptyList
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel = koinViewModel()) {

    val cityName = viewModel.cityName.collectAsState()
    val searchByName = viewModel.searchByName.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val permissionState =
        rememberPermissionState(
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
            onPermissionResult = { isGranted ->
                if (isGranted) {
                    viewModel.fetchCurrentLocation(context) { location ->
                        if (location == null) return@fetchCurrentLocation
                        val geoLocation = viewModel.getCityAndCountry(location, context)
                        navController.navigate(NavRoutes.DETAILS.route + "/$geoLocation")
                    }
                }
            })

    Column(
        modifier = Modifier
            .fillMaxSize().background(color = Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                singleLine = true,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.weight(1f),
                value = cityName.value,
                onValueChange = { viewModel.setCityName(it) },
                label = { Text(text = stringResource(R.string.add_hint), color = Color.Gray) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.width(5.dp))

            IconButton(
                onClick = {
                    if (cityName.value.isNotBlank()) {
                        viewModel.addCity(cityName.value)
                        viewModel.setCityName("")
                    }

                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add city",
                    tint = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            IconButton(
                onClick = { viewModel.refreshCities() }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh cities",
                    tint = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            IconButton(
                onClick = {
                    if (permissionState.status.isGranted) {
                        viewModel.fetchCurrentLocation(context) { location ->
                            if (location == null)
                                return@fetchCurrentLocation
                            // get city and country using latitude and longitude
                            val geoLocation = viewModel.getCityAndCountry(location, context)
                            navController.navigate(NavRoutes.DETAILS.route + "/$geoLocation")

                            // using latitude and longitude
                            //navController.navigate(NavRoutes.DETAILS.route + "/${location.latitude},${location.longitude}")
                        }
                    } else {
                        permissionState.launchPermissionRequest()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "location",
                    tint = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.fillMaxWidth(),
            value = searchByName.value,
            onValueChange = { viewModel.search(it) },
            label = { Text(text = stringResource(R.string.search_hint), color = Color.Gray) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (val state = uiState) {
                is WeatherUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)
                    )
                }

                is WeatherUiState.Success -> {
                    val cities = state.data
                    if (cities.isEmpty()) {
                        EmptyList()
                    } else {
                        CityList(
                            cities = cities,
                            onRemoveCity = { city -> viewModel.removeCity(city) },
                            navController = navController
                        )
                    }
                }

                is WeatherUiState.Error -> {
                    ErrorMessage(
                        error = state.error,
                        onRetry = { viewModel.refreshCities() }
                    )
                }

                else -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun CityList(
    cities: List<CityWeatherModel>,
    onRemoveCity: (String) -> Unit,
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cities) { cityData ->
            CityCard(
                cityName = cityData.data.request[0].query,
                weatherModel = cityData,
                onRemove = { onRemoveCity(cityData.data.request[0].query) },
                onClick = {
                    navController.navigate(NavRoutes.DETAILS.route + "/${cityData.data.request[0].query}")
                }
            )
        }
    }
}





