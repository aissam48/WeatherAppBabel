package com.android.weatherapp

import com.android.weatherapp.network.ApiManager
import com.android.weatherapp.network.models.CityWeatherModel
import com.android.weatherapp.network.models.Data
import com.android.weatherapp.network.models.Weather
import com.android.weatherapp.network.models.error_model.ErrorModel
import com.android.weatherapp.presentation.ui.details.DetailsViewModel
import com.android.weatherapp.presentation.ui.details.ForecastUiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
class DetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @MockK
    lateinit var apiManager: ApiManager

    private lateinit var viewModel: DetailsViewModel

    private val mockForecastModel = mockk<CityWeatherModel>()
    private val mockError = mockk<ErrorModel>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        val mockWeatherList = List(5) { mockk<Weather>() }
        val mockData = mockk<Data> {
            every { weather } returns mockWeatherList
        }
        every { mockForecastModel.data } returns mockData

        coEvery {
            apiManager.getCityWeather("Rabat, Morocco",
                5,
                any(),
                any())
        } coAnswers {
            val onSuccess = arg<(CityWeatherModel) -> Unit>(2)
            onSuccess(mockForecastModel)
        }

        viewModel = DetailsViewModel(apiManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun getForecastOfCity() = runTest {

        viewModel.getCityForecast("Rabat, Morocco",)
        val state = viewModel.uiState.filterIsInstance<ForecastUiState.Success>().first()
        assertEquals(5, state.data.data.weather.size)

    }


}