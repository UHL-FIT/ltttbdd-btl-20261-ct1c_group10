package com.example.ungdungthoitiet.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
//Interface khai báo cấu trúc các điểm cuối (Endpoints) gọi lên máy chủ OpenWeather API qua thư viện Retrofit.
interface OpenWeatherApi {
    //Thực hiện phương thức HTTP GET lên đường dẫn "data/2.5/weather" để lấy dữ liệu thời tiết hiện tại của thành phố chỉ định. Trả về đối tượng cấu trúc mạng WeatherResponse
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "vi"
    ): WeatherResponse

    //Gửi yêu cầu HTTP GET lên "data/2.5/forecast" lấy dữ liệu chuỗi thiên văn dự báo tương lai. Trả về lớp cấu trúc ForecastResponse
    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        //units = "metric" để ép server trả về đơn vị hệ mét (độ C, m/s) và lang = "vi" để lấy mô tả thời tiết bằng tiếng Việt.
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "vi"
    ): ForecastResponse

    //Gửi yêu cầu HTTP GET lên "geo/1.0/direct" để tìm kiếm danh sách tọa độ vị trí địa lý chuẩn hóa từ chuỗi chữ nhập vào. Trả về mảng danh sách List<GeocodingResponse>
    @GET("geo/1.0/direct")
    suspend fun getGeocoding(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey: String
    ): List<GeocodingResponse>
}
