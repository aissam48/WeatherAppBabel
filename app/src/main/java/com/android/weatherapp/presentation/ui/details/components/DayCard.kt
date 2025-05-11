package com.android.weatherapp.presentation.ui.details.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.weatherapp.network.models.Weather
import com.android.weatherapp.utils.Helper


@Composable
fun DayCard(weather: Weather, selectedDay: Weather?, clicked: (weather: Weather) -> Unit) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(50.dp)
            .clickable {
                clicked(weather)
            },
        colors = CardDefaults.cardColors(
            containerColor = if (selectedDay == weather) Color.Gray else Color.LightGray
        )
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = Helper().getDayName(weather.date),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "${weather.mintempC}°",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "${weather.maxtempC}°",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
    Spacer(modifier = Modifier.width(15.dp))
}