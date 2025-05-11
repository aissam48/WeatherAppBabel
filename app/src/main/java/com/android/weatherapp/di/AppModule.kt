package com.android.weatherapp.di

import com.android.weatherapp.network.ApiManager
import com.android.weatherapp.presentation.ui.details.DetailsViewModel
import com.android.weatherapp.presentation.ui.main.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {

    single { ApiManager() }

    viewModelOf(::MainViewModel)
    viewModelOf(::DetailsViewModel)

}