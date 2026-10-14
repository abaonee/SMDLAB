package uz.ttpu.weatherpatterns.data.repository

import kotlinx.coroutines.CancellationException
import uz.ttpu.weatherpatterns.data.api.WeatherApi
import uz.ttpu.weatherpatterns.data.model.Weather

class WeatherRepository(private val api: WeatherApi) {
    suspend fun getWeather(city: String): Result<Weather> {
        return try {
            Result.success(api.fetchWeather(city))
        } catch (e: CancellationException) {
            throw e
        } catch (e: IllegalArgumentException) {
            Result.failure(e)
        }
    }
}
