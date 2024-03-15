package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.RetrofitClient
import com.example.myapplication.RetrofitServices
import com.example.myapplication.WeatherData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val retrofitService: RetrofitServices = RetrofitClient.getClient("https://api.openweathermap.org/data/2.5/").create(
        RetrofitServices::class.java)
    val weatherData: MutableLiveData<WeatherData> = MutableLiveData()
        //MutableLiveData - это класс, который используется для хранения и обновления данных в Android-приложениях
    //создается новый экземпляр MutableLiveData и инициализируется с помощью конструктора по умолчанию.
    // Это означает, что переменная weatherData будет хранить данные типа WeatherData, которые могут быть изменены в любое время.
    //MutableLiveData является подклассом LiveData, который также используется для хранения данных в Android-приложениях. Однако,
    // в отличие от LiveData,
    // MutableLiveData позволяет изменять данные внутри себя,
    // что делает его более удобным для использования в приложениях, которые должны обновлять данные в реальном времени.

    fun getCurrentWeather(cityName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = retrofitService.getCurrentWeather(cityName, "ec9102a991f613fe3368ac64d021a84a", "metric", "ru")
                weatherData.postValue(response)
            }
            catch (ex : Exception) {}
        }
    }
}