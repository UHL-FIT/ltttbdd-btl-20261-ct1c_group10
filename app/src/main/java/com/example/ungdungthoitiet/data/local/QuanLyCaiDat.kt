package com.example.ungdungthoitiet.data.local
//Để lưu giữ bền vững các tùy chọn cài đặt cấu hình nhỏ dưới dạng cặp khóa - giá trị (Key - Value) trên bộ nhớ máy
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Khởi tạo DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "cai_dat_thoi_tiet")

// Các Key lưu trữ hệ thống đơn vị đo
private val KEY_DON_VI_NHIET_DO = stringPreferencesKey("don_vi_nhiet_do")
private val KEY_DON_VI_GIO = stringPreferencesKey("don_vi_gio")
private val KEY_DON_VI_LUONG_MUA = stringPreferencesKey("don_vi_luong_mua")
private val KEY_DON_VI_KHOANG_CACH = stringPreferencesKey("don_vi_khoang_cach")

class QuanLyCaiDat(private val context: Context) {

    // Đọc dữ liệu đơn vị Nhiệt độ
    //Sử dụng toán tử chuyển đổi dữ liệu .map để đọc giá trị lưu trữ trong DataStore dưới dạng luồng dữ liệu bất đồng bộ. Nếu giá trị chưa từng được lưu trữ trước đó (null), toán tử Elvis (?:) sẽ tự động gán giá trị mặc định cho hệ thống (ví dụ: "°C", "km/h", "mm", "km")
    val donViNhietDo: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_DON_VI_NHIET_DO] ?: "°C"
    }

    // Đọc dữ liệu đơn vị Tốc độ gió
    val donViGio: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_DON_VI_GIO] ?: "km/h"
    }

    // Đọc dữ liệu đơn vị Lượng mưa
    val donViLuongMua: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_DON_VI_LUONG_MUA] ?: "mm"
    }

    // Đọc dữ liệu đơn vị Khoảng cách
    val donViKhoangCach: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_DON_VI_KHOANG_CACH] ?: "km"
    }

    // Các hàm lưu dữ liệu xuống DataStore
    //Phương thức ghi dữ liệu ghi đè: suspend fun
    //context.dataStore.edit để ghi đè chuỗi đơn vị mới do người dùng lựa chọn xuống ổ cứng lưu trữ.
    suspend fun luuDonViNhietDo(donVi: String) {
        context.dataStore.edit { preferences -> preferences[KEY_DON_VI_NHIET_DO] = donVi }
    }

    suspend fun luuDonViGio(donVi: String) {
        context.dataStore.edit { preferences -> preferences[KEY_DON_VI_GIO] = donVi }
    }

    suspend fun luuDonViLuongMua(donVi: String) {
        context.dataStore.edit { preferences -> preferences[KEY_DON_VI_LUONG_MUA] = donVi }
    }

    suspend fun luuDonViKhoangCach(donVi: String) {
        context.dataStore.edit { preferences -> preferences[KEY_DON_VI_KHOANG_CACH] = donVi }
    }
}