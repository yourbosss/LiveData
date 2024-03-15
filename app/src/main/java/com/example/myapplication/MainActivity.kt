package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import viewmodel.WeatherViewModel

class MainActivity : AppCompatActivity() {
    lateinit var editText : EditText
    lateinit var buttonWeather : Button
    lateinit var textViewCity: TextView
    lateinit var textViewTemperature: TextView
    lateinit var textViewDescription: TextView
    lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            //при новой ориентации создает новую активность экрана
        setContentView(R.layout.activity_main)

        textViewCity = findViewById(R.id.textView)
        textViewTemperature = findViewById(R.id.textView2)
        textViewDescription = findViewById(R.id.textView3)
        editText = findViewById(R.id.editTextText)
        buttonWeather = findViewById(R.id.button)
        image = findViewById(R.id.imageView)

        val weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
            //Нужен для получения экземпляра viewmodel
            //provide -класс для досупа к экземпляру
            //для получения экземпляра WeatherViewModel в Android-приложении
        // и дальнейшего использования его в коде для доступа к данным и логике приложения.
        buttonWeather.setOnClickListener() {
            if (editText.text.toString() != "") {
                weatherViewModel.getCurrentWeather(editText.text.toString())
            }
        }
        weatherViewModel.weatherData.observe(this, Observer { response ->
            val weather = response.weather[0]
            val main = response.main
            val iconc = response.weather[0]
                //каждый раз когда weatherdata будет изменяться  будут уведомлять
                //observe -наблюдать


            textViewCity.text = "Город: ${response.name}"
            textViewTemperature.text = "Температура: ${main.temp}°C"
            textViewDescription.text = "Описание: ${weather.description}"
            Picasso.get().load("https://openweathermap.org/img/wn/${iconc.icon}.png").into(image)
        }) // связывание пользовательких данных с шаблонами

    }
}

//Когда приложение отправляет запрос не сервер, данные часто приходят в формате JSON. Retrofit преобразует их в объекты,
// понятные приложению. Теперь оно может использовать эти объекты, чтобы, например, обновить что-то в интерфейсе
interface RetrofitServices { //создан для получения и преобразования api клиентов
    @GET("weather") //получение и создание weather
    suspend fun getCurrentWeather( //getweaher - возращает текущее метеоусловие Api
        //suspend - функция котороая может быть остановлена и возоблена позже для параллельных операций.
        @Query("q") cityName: String?, //в параметр city Мы закидывем которой нам дан в документе
        @Query("appid") apiKey: String?, // Тоже самое
        @Query("units") units: String, // тоже самое
        @Query("lang") lang: String // тоже самое
    ):WeatherData //в конце мы должны в конце получить это (закинет данные в класс)
}

object RetrofitClient { //создан чтобы отправлять запросы к серверу и делать остальные операции.
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit { //функция Для работы с view
        //передает ссылку и по этой ссылке и создается клиент
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl) //ссылка апи
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}
data class Weather( //создание класса для файла
    val description: String,
    val icon: String,
    val temp: Double
)
data class WeatherData( //преобразование предыдущих данных в это
    val weather: List<Weather>,
    val name: String,
    val main: Main
)
data class Main(
    val temp: Double,
)

