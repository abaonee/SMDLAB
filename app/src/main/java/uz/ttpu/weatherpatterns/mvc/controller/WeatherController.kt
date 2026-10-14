package uz.ttpu.weatherpatterns.mvc.controller

import kotlinx.coroutines.*
import uz.ttpu.weatherpatterns.mvc.model.WeatherModel
import uz.ttpu.weatherpatterns.mvc.view.WeatherView

class WeatherController(
    private val model: WeatherModel,
    private val view: WeatherView,
    private val scope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main),
) {
    fun onCitySelected(city: String) {
        scope.launch {
            model.startLoading()
            view.render()
            model.load(city)
            view.render()
        }
    }
}
