package com.example.ungdungthoitiet.data.local
//Khởi tạo, cấu hình hệ thống lưu trữ SQLite cục bộ bền vững bằng thư viện Room Database giúp ứng dụng có khả năng chạy offline không cần mạng.
import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//Thành phần thực thể ThanhPhoEntity: Định nghĩa cấu trúc bảng dữ liệu mang tên "thanh_pho_yeu_thich". Biến tenThanhPho được gán annotation
//@PrimaryKey làm khóa chính duy nhất của một hàng dữ liệu trong SQLite. Các biến khác lưu trữ trực tiếp các chỉ số thời tiết offline
// (nhietDo, trangThai, doAm, tocDoGio, apSuat, nhietDoCaoNhat, nhietDoThapNhat, iconId, duBaoTheoGio, duBaoTheoTuan).
@Entity(tableName = "thanh_pho_yeu_thich")
data class ThanhPhoEntity(
    @PrimaryKey val tenThanhPho: String,
    val nhietDo: Int = 0,                             // Lưu trữ nhiệt độ offline
    val trangThai: String = "",                       // Lưu trữ trạng thái chữ offline
    val doAm: Int = 0,                                 // Lưu trữ độ ẩm offline
    val tocDoGio: Double = 0.0,                       // Lưu trữ tốc độ gió offline
    val apSuat: Int = 0,                              // Lưu trữ áp suất khí quyển offline
    val nhietDoCaoNhat: Int = 0,                      // Lưu trữ nhiệt độ cao nhất offline
    val nhietDoThapNhat: Int = 0,                     // Lưu trữ nhiệt độ thấp nhất offline
    val iconId: String = "01d",                       // Lưu trữ mã hiệu icon để Coil render khi mất mạng
    val duBaoTheoGio: List<String> = emptyList(),     // Chuyển đổi danh sách giờ sang JSON chuỗi để lưu cố định
    val duBaoTheoTuan: List<String> = emptyList()     // Chuyển đổi danh sách tuần sang JSON chuỗi để lưu cố định
)

//Thành phần bộ chuyển đổi Converters: SQLite không hỗ trợ lưu trữ trực tiếp kiểu dữ liệu mảng đối tượng List<String>.
class Converters {
    //fromString(value: String): List<String>: Đọc chuỗi văn bản JSON từ SQLite ra và giải mã cấu trúc ngược lại thành
    //mảng List<String> trong Kotlin.
    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
    //fromList(list: List<String>): String: Dùng thư viện Gson chuyển đổi mảng danh sách thành một chuỗi văn bản
    //thuần JSON duy nhất để lưu gọn vào SQLite
    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }
}

@Dao
//Interface định nghĩa các câu lệnh SQL tương tác trực tiếp với cơ sở dữ liệu.
interface ThanhPhoDao {
    @Query("SELECT * FROM thanh_pho_yeu_thich")
    suspend fun layTatCa(): List<ThanhPhoEntity>
    //onConflict = OnConflictStrategy.REPLACE nghĩa là nếu trùng tên thành phố (khóa chính), nó sẽ tự động ghi đè bản ghi mới nhất lên dữ liệu cũ
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun themThanhPho(thanhPho: ThanhPhoEntity)
    @Query("DELETE FROM thanh_pho_yeu_thich WHERE tenThanhPho = :ten")
    suspend fun xoaThanhPho(ten: String)
}

//AppDatabase (Cơ sở dữ liệu chính): Lớp trừu tượng kế thừa RoomDatabase cấu hình phiên bản version = 2
@Database(entities = [ThanhPhoEntity::class], version = 2, exportSchema = false) // Tăng version lên lớp 2 hỗ trợ bộ đệm offline
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun thanhPhoDao(): ThanhPhoDao

    companion object {
        //@Volatile private var INSTANCE và khối synchronized(this) để đảm bảo rằng trong suốt vòng đời ứng dụng chỉ có duy nhất một đối tượng Database được tạo ra, tránh xung đột bộ nhớ và lãng phí tài nguyên.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun layDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "thoitiet_database"
                )
                    .fallbackToDestructiveMigration() // Hỗ trợ tự động xóa dọn sạch database phiên bản cũ, loại bỏ xung đột crash
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}