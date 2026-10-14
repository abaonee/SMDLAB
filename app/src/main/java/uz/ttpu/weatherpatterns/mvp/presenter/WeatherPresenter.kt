package uz.ttpu.weatherpatterns.mvp.presenter

import kotlinx.coroutines.*
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository
import uz.ttpu.weatherpatterns.mvp.WeatherContract

class WeatherPresenter(
    private val view: WeatherContract.View,
    private val repository: WeatherRepository,
    private val scope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main),
) {
    fun onCitySelected(city: String) {
        view.showLoading()
        scope.launch {
            repository.getWeather(city)
                .onSuccess { view.showWeather(it) }
                .onFailure { view.showError(it.message ?: "Unknown error") }
        }
    }

    fun destroy() {
        scope.cancel()
    }
}
