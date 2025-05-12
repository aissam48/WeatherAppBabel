package com.android.weatherapp.presentation.ui.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.weatherapp.R
import com.android.weatherapp.network.models.CityWeatherModel

@Composable
fun CityCard(
    cityName: String,
    weatherModel: CityWeatherModel,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cityName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove city",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(
                    R.string.temperature_c,
                    weatherModel.data.current_condition[0].temp_C
                ),
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                text = stringResource(
                    R.string.weather,
                    weatherModel.data.current_condition[0].weatherDesc[0].value
                ),
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                text = stringResource(
                    R.string.humidity,
                    weatherModel.data.current_condition[0].humidity
                ),
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                text = stringResource(
                    R.string.wind_speed_kmph,
                    weatherModel.data.current_condition[0].windspeedKmph
                ),
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}