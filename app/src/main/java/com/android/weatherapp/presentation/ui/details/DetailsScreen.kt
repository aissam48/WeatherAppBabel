package com.android.weatherapp.presentation.ui.details

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.android.weatherapp.R
import com.android.weatherapp.network.models.CityWeatherModel
import com.android.weatherapp.presentation.ui.common_components.ErrorMessage
import com.android.weatherapp.presentation.ui.details.components.DayCard
import com.android.weatherapp.presentation.ui.details.components.HourCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(
    navController: NavHostController,
    viewModel: DetailsViewModel = koinViewModel(),
    cityName: String
) {

    val uiState by viewModel.uiState.collectAsState()

    val selectedDay = viewModel.selectedDay.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCityForecast(cityName)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.DarkGray
                )
            }
            Text(
                text = cityName,
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontSize = 30.sp,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { viewModel.refreshCity(cityName) }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh city",
                    tint = Color.DarkGray
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {

            when (val state = uiState) {

                is ForecastUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)
                    )
                }

                is ForecastUiState.Success -> {
                    val data = state.data
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            CityCurrentInfo(data)
                        }

                        item {
                            LazyRow(modifier = Modifier.fillMaxWidth()) {

                                items(data.data.weather) { weather ->
                                    DayCard(weather, selectedDay.value) { clickedItem ->
                                        viewModel.setDay(clickedItem)
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(15.dp))
                        }

                        items(selectedDay.value?.hourly ?: listOf()) { hourly ->
                            HourCard(hourly)
                        }
                    }
                }

                is ForecastUiState.Error -> {
                    ErrorMessage(
                        error = state.error,
                        onRetry = { viewModel.refreshCity(cityName) }
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
fun CityCurrentInfo(data: CityWeatherModel) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = data.data.current_condition[0].observation_time,
            color = Color.Black,
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "${data.data.current_condition[0].temp_C}°",
            color = Color.Black,
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = data.data.current_condition[0].weatherDesc[0].value,
            color = Color.Black,
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Text(
                text = stringResource(
                    R.string.humidity_char,
                    data.data.current_condition[0].humidity
                ),
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = stringResource(
                    R.string.wind_speed_kmph,
                    data.data.current_condition[0].windspeedKmph
                ),
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = stringResource(R.string.pressure, data.data.current_condition[0].pressure),
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Text(
                text = stringResource(R.string.sunrise, data.data.weather[0].astronomy[0].sunrise),
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = stringResource(R.string.sunset, data.data.weather[0].astronomy[0].sunset),
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Text(
                text = stringResource(R.string.maxtempc, data.data.weather[0].maxtempC),
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = stringResource(R.string.mintempc, data.data.weather[0].mintempC),
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(25.dp))
    }
}

