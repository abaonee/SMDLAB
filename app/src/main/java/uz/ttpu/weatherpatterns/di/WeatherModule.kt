package uz.ttpu.weatherpatterns.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import uz.ttpu.weatherpatterns.data.api.FakeWeatherApi
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository
import uz.ttpu.weatherpatterns.mvi.viewmodel.WeatherMviViewModel
import uz.ttpu.weatherpatterns.mvvm.viewmodel.WeatherViewModel

object WeatherModule {
    val repository = WeatherRepository(FakeWeatherApi(latencyMs = 3_000))

    val weatherViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer { WeatherViewModel(repository) }
    }

    val weatherMviViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer { WeatherMviViewModel(repository) }
    }
}
