package com.android.weatherapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.weatherapp.network.ApiManager
import com.android.weatherapp.network.models.CityWeatherModel
import com.android.weatherapp.network.models.Data
import com.android.weatherapp.network.models.error_model.ErrorModel
import com.android.weatherapp.presentation.ui.main.MainViewModel
import com.android.weatherapp.presentation.ui.main.WeatherUiState
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var apiManager: ApiManager

    private lateinit var viewModel: MainViewModel

    private val testCityName = "Test City, Test Country"
    private val mockWeatherModel = mockk<CityWeatherModel>()
    private val mockError = mockk<ErrorModel>()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        coEvery {
            apiManager.getCityWeather(any(), any(), any(), any())
        } answers {
            val onSuccess = arg<(CityWeatherModel) -> Unit>(2)
            onSuccess(mockWeatherModel)
        }

        viewModel = MainViewModel(apiManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun fetchDefaultCitiesWithSuccess() = runTest {
        val state = viewModel.uiState
            .filterIsInstance<WeatherUiState.Success>()
            .first()

        println("MainViewModelTest ${state.data.size}")
        assertEquals(5, state.data.size)

        coVerify(exactly = 5) {
            apiManager.getCityWeather(any(), any(), any(), any())
        }
    }

    @Test
    fun fetchDefaultCitiesWithError() = runTest {
        coEvery { apiManager.getCityWeather(any(), any(), any(), any()) } answers {
            val onError = arg<(ErrorModel) -> Unit>(3)
            onError(mockError)
        }
        val errorViewModel = MainViewModel(apiManager)

        val state = errorViewModel.uiState
            .filterIsInstance<WeatherUiState.Error>()
            .first()
        println("MainViewModelTest $state")

        assertEquals(mockError, state.error)
    }

    @Test
    fun testAddNewCity() = runTest {

        val initState = viewModel.uiState.filterIsInstance<WeatherUiState.Success>().first()
        val getInitSize = initState.data.size
        viewModel.addCity(testCityName)
        val updateState = viewModel.uiState.filterIsInstance<WeatherUiState.Success>().first {
            it.data.size > getInitSize
        }
        val getUpdateSize = updateState.data.size
        assertEquals(getInitSize + 1, getUpdateSize)

        coVerify { apiManager.getCityWeather(testCityName, 1, any(), any()) }

    }

}