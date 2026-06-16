package com.example.ungdungthoitiet.data.remote
//Khai báo các Data Class khớp chính xác với cây cấu trúc dữ liệu JSON trả về từ API, phục vụ cho bộ chuyển đổi Gson Converter tự động phân rã chuỗi.
import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val timezone: Long,
    val name: String
)

data class Main(
    val temp: Double,
    //Sử dụng annotation @SerializedName("temp_min") và @SerializedName("temp_max") trong lớp Main để ánh xạ chính xác khóa viết kiểu gạch nối của JSON (temp_min) sang biến viết kiểu Lạc đà (tempMin) trong Kotlin.
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    val humidity: Int,
    val pressure: Int
)

data class Weather(
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double
)

data class ForecastResponse(
    val list: List<ForecastItem>,
    val city: City
)

data class ForecastItem(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    @SerializedName("dt_txt") val dtTxt: String
)

data class City(
    val timezone: Long
)

data class GeocodingResponse(
    val name: String,
    val country: String,
    val state: String? = null
)
