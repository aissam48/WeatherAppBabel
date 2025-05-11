package com.android.weatherapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.android.weatherapp.presentation.ui.details.DetailsScreen
import com.android.weatherapp.presentation.ui.main.MainScreen

@Composable
fun NavigationHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = NavRoutes.MAIN.route,
    ) {

        composable(NavRoutes.MAIN.route) {
            MainScreen(navController = navController)
        }

        composable("${NavRoutes.DETAILS.route}/{cityName}") {
            val cityName = it.arguments?.getString("cityName") ?: ""
            DetailsScreen(navController = navController, cityName = cityName)
        }

    }
}
